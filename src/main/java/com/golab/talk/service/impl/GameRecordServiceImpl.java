package com.golab.talk.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.GameRecord;
import com.golab.talk.dto.GameRecordDto;
import com.golab.talk.dto.GameRecordWithRankDto;
import com.golab.talk.dto.PlayerDto;
import com.golab.talk.repository.GameRecordRepository;
import com.golab.talk.service.GameRecordService;

@Service
public class GameRecordServiceImpl implements GameRecordService {

	@Autowired
	private GameRecordRepository gameRecordRepository;

	@Override
	public void createGameRecord(GameRecordDto gameRecordDto) {
		GameRecord gameRecord = new GameRecord(gameRecordDto.getUserId(), 0, 0, 1000);
		gameRecordRepository.save(gameRecord);
	}

	@Override
	public GameRecordWithRankDto findByUserId(String userId) {

		Map<String, Object> result = gameRecordRepository.findByUserIdWithRanking(userId);

		if (result != null) {
			GameRecordWithRankDto gameRecordWithRankDto = new GameRecordWithRankDto(
				String.valueOf(result.get("userId")),
				(int)result.get("games"),
				(int)result.get("wins"),
				(int)result.get("mmr"),
				(Integer.parseInt(String.valueOf(result.get("ranking"))))
			);
			return gameRecordWithRankDto;
		}

		return null;
	}

	@Override
	public List<GameRecordDto> findMMR(String userId1, String userId2) {
		List<GameRecord> gameRecords = gameRecordRepository.findMMRWithRanking(userId1, userId2);
		// GameRecord를 GameRecordDto로 변환하여 리스트로 반환
		return gameRecords.stream().map(gameRecord -> new GameRecordDto(
				gameRecord.getUserId(),
				gameRecord.getGames(),
				gameRecord.getWins(),
				gameRecord.getMmr()
			))
			.collect(Collectors.toList());
	}

	@Override
	public List<GameRecordWithRankDto> showGameRecordList() {
		List<Map<String, Object>> gameRecords = gameRecordRepository.findAllByOrderByMmrAscWithRanking();
		// GameRecord를 GameRecordWithRankDto로 변환하여 리스트로 반환

		return gameRecords.stream().map(gameRecord -> new GameRecordWithRankDto(
				String.valueOf(gameRecord.get("userId")),
				(int)gameRecord.get("games"),
				(int)gameRecord.get("wins"),
				(int)gameRecord.get("mmr"),
				(Integer.parseInt(String.valueOf(gameRecord.get("ranking"))))
			))
			.collect(Collectors.toList());
	}

	@Override
	public void updateRecord(PlayerDto playerDto) {
		String winnerId = playerDto.getWinnerId();
		String loserId = playerDto.getLoserId();

		GameRecordWithRankDto winner = findByUserId(winnerId);
		GameRecordWithRankDto loser = findByUserId(loserId);
		System.out.println("winner: " + winner.toString());
		System.out.println("loser: " + loser.toString());

		if (winner != null && loser != null) {
			winner.setGames(winner.getGames() + 1);
			winner.setWins(winner.getWins() + 1);
			winner.setMmr((int)calcRating(winner.getMmr(), loser.getMmr(), 1));

			gameRecordRepository.save(new GameRecord(
				winner.getUserId(),
				winner.getGames(),
				winner.getWins(),
				winner.getMmr()
			));

			loser.setGames(loser.getGames() + 1);
			loser.setMmr((int)calcRating(loser.getMmr(), winner.getMmr(), 0));

			gameRecordRepository.save(new GameRecord(
				loser.getUserId(),
				loser.getGames(),
				loser.getWins(),
				loser.getMmr()
			));

			System.out.println("winner: " + winner.toString());
			System.out.println("loser: " + loser.toString());
		} else {
			System.out.println("전적을 찾을 수 없습니다.");
		}
	}

	public double calcRating(double myRating, double enemyRating, int isWin) {
		return myRating + 40 * (isWin - calcEWR(myRating, enemyRating));
	}

	// Calculating expected winning rate
	public double calcEWR(double myRating, double enemyRating) {
		return 1 / (Math.pow(10, (enemyRating - myRating) / 400) + 1);
	}

}
