package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.GameRecordDto;
import com.golab.talk.dto.GameRecordWithRankDto;
import com.golab.talk.dto.PlayerDto;

public interface GameRecordService {

	void createGameRecord(GameRecordDto gameRecordDto);

	GameRecordWithRankDto findByUserId(String userId);

	List<GameRecordDto> findMMR(String userId1, String userId2);

	List<GameRecordWithRankDto> showGameRecordList();

	void updateRecord(PlayerDto playerDto);

}
