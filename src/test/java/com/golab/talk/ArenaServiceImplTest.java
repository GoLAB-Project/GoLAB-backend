package com.golab.talk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.golab.talk.dto.ArenaMatchResponse;
import com.golab.talk.service.impl.ArenaServiceImpl;

class ArenaServiceImplTest {

	@Test
	void threeSpeechesFinishPracticeMatch() {
		ArenaServiceImpl service = new ArenaServiceImpl(message -> null);
		ArenaMatchResponse start = service.start();

		assertFalse(start.isFinished());
		assertEquals("OPENING", start.getPhase());
		assertTrue(start.isPlayerTurn());

		ArenaMatchResponse opening = service.speak(start.getMatchId(),
			"이 주제는 시간이 핵심이다. 강제하면 선택이 없어진다.");
		assertEquals("CROSS", opening.getPhase());

		ArenaMatchResponse cross = service.speak(start.getMatchId(),
			"방금 상대 말은 결과를 빠뜨렸다. 시간이 바로 줄어든다.");
		assertEquals("CLOSING", cross.getPhase());

		ArenaMatchResponse closing = service.speak(start.getMatchId(),
			"한 줄로, 시간이 내 편이 되려면 강제면 안 된다.");

		assertTrue(closing.isFinished());
		assertEquals("VERDICT", closing.getPhase());
		assertFalse(closing.isPlayerTurn());
		assertTrue(closing.getLines().size() >= 6);
	}
}
