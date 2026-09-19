package com.golab.talk.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.golab.talk.domain.ArenaLine;
import com.golab.talk.domain.ArenaMatch;
import com.golab.talk.domain.ArenaPhase;
import com.golab.talk.dto.ArenaLineDto;
import com.golab.talk.dto.ArenaMatchResponse;
import com.golab.talk.service.ArenaService;
import com.golab.talk.service.GPTService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArenaServiceImpl implements ArenaService {

	private static final Pattern JSON_BLOCK = Pattern.compile("\\{[\\s\\S]*\\}");

	private static final List<String> TOPICS = Arrays.asList(
		"야자는 강제여야 한다",
		"숙제는 없애는 게 낫다",
		"교복은 필요하다",
		"SNS는 10대에게 이롭다",
		"시험은 상대평가가 맞다",
		"AI로 숙제하는 건 부정행위다"
	);

	private static final List<String> PERSONAS = Arrays.asList(
		"냉정한 검사",
		"인터넷 댓글러",
		"깐깐한 교수"
	);

	private static final String[][] CONSTRAINTS = {
		{"금기어: \"왜냐하면\" 사용 금지", "왜냐하면", null},
		{"필수어: \"시간\"을 한 번은 써야 함", null, "시간"},
		{"스타일: 비유로만 말하기", null, null}
	};

	private final GPTService gptService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ConcurrentMap<String, ArenaMatch> matches = new ConcurrentHashMap<>();

	@Override
	public ArenaMatchResponse start() {
		ArenaMatch match = new ArenaMatch();
		match.setId(UUID.randomUUID().toString());
		match.setTopic(pick(TOPICS));
		boolean playerAgrees = ThreadLocalRandom.current().nextBoolean();
		match.setPlayerStance(playerAgrees ? "찬성" : "반대");
		match.setAiStance(playerAgrees ? "반대" : "찬성");
		String[] constraint = CONSTRAINTS[ThreadLocalRandom.current().nextInt(CONSTRAINTS.length)];
		match.setConstraint(constraint[0]);
		match.setForbiddenWord(constraint[1]);
		match.setRequiredWord(constraint[2]);
		match.setPersona(pick(PERSONAS));
		matches.put(match.getId(), match);
		return toResponse(match);
	}

	@Override
	public ArenaMatchResponse get(String matchId) {
		return toResponse(require(matchId));
	}

	@Override
	public ArenaMatchResponse speak(String matchId, String text) {
		ArenaMatch match = require(matchId);
		synchronized (match) {
			if (match.isFinished() || match.getPhase() == ArenaPhase.VERDICT) {
				return toResponse(match);
			}
			String playerText = text == null ? "" : text.trim();
			if (match.getRequiredWord() != null && playerText.contains(match.getRequiredWord())) {
				match.setPlayerUsedRequiredWord(true);
			}
			int playerGain = scoreAndAppend(match, ArenaLine.PLAYER, playerText);
			match.setPlayerScore(match.getPlayerScore() + playerGain);

			String aiText = opponentSpeech(match, playerText);
			int aiGain = scoreAndAppend(match, ArenaLine.AI, aiText);
			match.setAiScore(match.getAiScore() + aiGain);

			match.setPhase(match.getPhase().next());
			if (match.getPhase() == ArenaPhase.VERDICT) {
				finish(match);
			}
			return toResponse(match);
		}
	}

	private ArenaMatch require(String matchId) {
		ArenaMatch match = matches.get(matchId);
		if (match == null) {
			throw new IllegalArgumentException("진행 중인 연습전이 없습니다.");
		}
		return match;
	}

	private int scoreAndAppend(ArenaMatch match, String speaker, String text) {
		JudgeScore score = judge(match, speaker, text);
		match.getLines().add(new ArenaLine(speaker, match.getPhase(), blankToSilence(text)));
		match.getLines().add(new ArenaLine(ArenaLine.JUDGE, match.getPhase(), score.comment));
		return score.total();
	}

	private void finish(ArenaMatch match) {
		if (match.getRequiredWord() != null && !match.isPlayerUsedRequiredWord()) {
			match.setPlayerScore(Math.max(0, match.getPlayerScore() - 1));
			match.getLines().add(new ArenaLine(
				ArenaLine.JUDGE,
				ArenaPhase.VERDICT,
				"필수어 \"" + match.getRequiredWord() + "\"를 쓰지 않아 1점 감점."
			));
		}
		match.setFinished(true);
		if (match.getPlayerScore() > match.getAiScore()) {
			match.setWinner("PLAYER");
			match.setVerdict("배심원이 당신 쪽으로 기울었습니다. 반박이 상대 말을 실제로 받았습니다.");
		} else if (match.getPlayerScore() < match.getAiScore()) {
			match.setWinner("AI");
			match.setVerdict(match.getPersona() + " 쪽이 더 자주 핵심을 찔렀습니다. 다음엔 상대 마지막 문장부터 받아 치세요.");
		} else {
			match.setWinner("DRAW");
			match.setVerdict("점수는 같습니다. 훅 한 줄이 다음 판을 가릅니다.");
		}
	}

	private JudgeScore judge(ArenaMatch match, String speaker, String text) {
		JudgeScore gpt = judgeWithGpt(match, speaker, text);
		JudgeScore local = judgeLocal(match, speaker, text);
		if (gpt == null) {
			return local;
		}
		gpt.comment = gpt.comment == null || gpt.comment.isBlank() ? local.comment : gpt.comment;
		return gpt;
	}

	private JudgeScore judgeWithGpt(ArenaMatch match, String speaker, String text) {
		String prompt = "토론 한 수를 채점해. JSON만 답해. 키: relevance(0-3), rebuttal(0-3), hook(0-2), fallacy(0또는1), comment(한글 한 줄)."
			+ "\n주제: " + match.getTopic()
			+ "\n입장: " + (ArenaLine.PLAYER.equals(speaker) ? match.getPlayerStance() : match.getAiStance())
			+ "\n페이즈: " + match.getPhase()
			+ "\n제약: " + match.getConstraint()
			+ "\n발언: " + blankToSilence(text);
		String raw = safeGpt(prompt);
		if (raw == null) {
			return null;
		}
		try {
			Matcher matcher = JSON_BLOCK.matcher(raw);
			if (!matcher.find()) {
				return null;
			}
			JsonNode node = objectMapper.readTree(matcher.group());
			JudgeScore score = new JudgeScore();
			score.relevance = clamp(node.path("relevance").asInt(1), 0, 3);
			score.rebuttal = clamp(node.path("rebuttal").asInt(1), 0, 3);
			score.hook = clamp(node.path("hook").asInt(0), 0, 2);
			score.fallacy = clamp(node.path("fallacy").asInt(0), 0, 1);
			score.comment = node.path("comment").asText("관련성 " + score.relevance + ", 반박 " + score.rebuttal);
			applyConstraintPenalty(match, text, score);
			return score;
		} catch (Exception e) {
			log.warn("심판 JSON 파싱 실패: {}", e.getMessage());
			return null;
		}
	}

	private JudgeScore judgeLocal(ArenaMatch match, String speaker, String text) {
		JudgeScore score = new JudgeScore();
		String value = text == null ? "" : text.trim();
		if (value.isEmpty()) {
			score.comment = "침묵은 반박이 아니다. 관련성 0.";
			return score;
		}
		score.relevance = overlapScore(value, match.getTopic());
		String lastOpponent = lastSpeech(match, ArenaLine.PLAYER.equals(speaker) ? ArenaLine.AI : ArenaLine.PLAYER);
		if (match.getPhase() == ArenaPhase.OPENING) {
			score.rebuttal = value.length() >= 20 ? 1 : 0;
		} else if (lastOpponent != null && containsSnippet(value, lastOpponent)) {
			score.rebuttal = 3;
		} else if (match.getPhase() == ArenaPhase.CROSS) {
			score.rebuttal = 1;
		}
		if (match.getPhase() == ArenaPhase.CLOSING) {
			score.hook = value.length() >= 12 && value.length() <= 80 ? 2 : 1;
		}
		if (value.contains("바보") || value.contains("멍청")) {
			score.fallacy = 1;
		}
		applyConstraintPenalty(match, value, score);
		score.comment = "관련성 " + score.relevance + " · 반박 " + score.rebuttal
			+ (score.hook > 0 ? " · 훅 " + score.hook : "")
			+ (score.fallacy > 0 ? " · 오류 -1" : "");
		return score;
	}

	private void applyConstraintPenalty(ArenaMatch match, String text, JudgeScore score) {
		if (match.getForbiddenWord() != null && text != null && text.contains(match.getForbiddenWord())) {
			score.fallacy = 1;
			score.comment = (score.comment == null ? "" : score.comment + " ") + "금기어 사용.";
		}
	}

	private String opponentSpeech(ArenaMatch match, String playerText) {
		String prompt = "너는 토론 게임 상대다. 페르소나: " + match.getPersona()
			+ ". 주제: " + match.getTopic()
			+ ". 네 입장: " + match.getAiStance()
			+ ". 상대 입장: " + match.getPlayerStance()
			+ ". 페이즈: " + match.getPhase()
			+ ". 제약: " + match.getConstraint()
			+ ". 상대 발언: " + blankToSilence(playerText)
			+ "\n한국어만. 오프닝 2문장, 크로스 1-2문장(상대 말을 받아), 클로징 1문장. 다른 설명 금지.";
		String gpt = safeGpt(prompt);
		if (gpt != null && !gpt.isBlank()) {
			return trimSpeech(gpt);
		}
		return fallbackOpponent(match, playerText);
	}

	private String fallbackOpponent(ArenaMatch match, String playerText) {
		String topic = match.getTopic();
		String stance = match.getAiStance();
		if (match.getPhase() == ArenaPhase.OPENING) {
			if ("인터넷 댓글러".equals(match.getPersona())) {
				return topic + "을 " + stance + "하는 게 상식이다. 반대편은 기분만 말하고 결과는 안 본다.";
			}
			if ("깐깐한 교수".equals(match.getPersona())) {
				return "먼저 정의를 고정하자. " + topic + "의 핵심은 선택권이다. 그래서 " + stance + "가 맞다.";
			}
			return topic + "은 증거로 가려야 한다. 비용과 시간을 보면 " + stance + " 쪽이 남는다.";
		}
		if (match.getPhase() == ArenaPhase.CROSS) {
			String snippet = snippet(playerText);
			return "방금 \"" + snippet + "\"라고 했는데, 그건 " + stance + " 쪽 현실을 빠뜨렸다. 결과가 반대다.";
		}
		return "한 줄로 정리하면, " + topic + "은 " + stance + "일 때 사람이 덜 다친다.";
	}

	private String safeGpt(String prompt) {
		try {
			String result = gptService.getChatResponse(prompt);
			if (result == null || result.isBlank()) {
				return null;
			}
			return result.trim();
		} catch (Exception e) {
			log.warn("GPT 호출 실패, 로컬 판정으로 진행: {}", e.getMessage());
			return null;
		}
	}

	private ArenaMatchResponse toResponse(ArenaMatch match) {
		List<ArenaLineDto> lines = match.getLines().stream()
			.map(line -> new ArenaLineDto(line.getSpeaker(), line.getPhase().name(), line.getText()))
			.collect(Collectors.toList());
		return ArenaMatchResponse.builder()
			.matchId(match.getId())
			.topic(match.getTopic())
			.playerStance(match.getPlayerStance())
			.aiStance(match.getAiStance())
			.constraint(match.getConstraint())
			.persona(match.getPersona())
			.phase(match.getPhase().name())
			.phaseHint(match.getPhase().getHint())
			.phaseSeconds(match.getPhase().getSeconds())
			.playerTurn(!match.isFinished() && match.getPhase() != ArenaPhase.VERDICT)
			.finished(match.isFinished())
			.winner(match.getWinner())
			.verdict(match.getVerdict())
			.playerScore(match.getPlayerScore())
			.aiScore(match.getAiScore())
			.lines(lines)
			.build();
	}

	private static String lastSpeech(ArenaMatch match, String speaker) {
		for (int i = match.getLines().size() - 1; i >= 0; i--) {
			ArenaLine line = match.getLines().get(i);
			if (speaker.equals(line.getSpeaker())) {
				return line.getText();
			}
		}
		return null;
	}

	private static boolean containsSnippet(String text, String opponent) {
		if (opponent == null || opponent.length() < 4) {
			return false;
		}
		String token = opponent.replaceAll("[\"'.!?]", " ");
		for (String word : token.split("\\s+")) {
			if (word.length() >= 2 && text.contains(word)) {
				return true;
			}
		}
		return false;
	}

	private static int overlapScore(String text, String topic) {
		int hits = 0;
		for (String word : topic.split("\\s+")) {
			String token = word.replaceAll("[은는이가을를]", "");
			if (token.length() >= 2 && text.contains(token)) {
				hits++;
			}
		}
		return clamp(hits + (text.length() >= 24 ? 1 : 0), 0, 3);
	}

	private static String snippet(String text) {
		String value = blankToSilence(text);
		return value.length() > 24 ? value.substring(0, 24) + "…" : value;
	}

	private static String blankToSilence(String text) {
		return text == null || text.isBlank() ? "(침묵)" : text.trim();
	}

	private static String trimSpeech(String text) {
		String cleaned = text.replaceAll("^[\"']|[\"']$", "").trim();
		if (cleaned.length() > 160) {
			int cut = cleaned.lastIndexOf('다', 160);
			cleaned = (cut > 40 ? cleaned.substring(0, cut + 1) : cleaned.substring(0, 160)) + "…";
		}
		return cleaned;
	}

	private static String pick(List<String> pool) {
		return pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
	}

	private static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	private static class JudgeScore {
		private int relevance;
		private int rebuttal;
		private int hook;
		private int fallacy;
		private String comment;

		private int total() {
			return Math.max(0, relevance + rebuttal + hook - fallacy);
		}
	}
}
