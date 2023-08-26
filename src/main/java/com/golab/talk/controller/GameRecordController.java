package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.GameRecordDto;
import com.golab.talk.dto.GameRecordWithRankDto;
import com.golab.talk.dto.PlayerDto;
import com.golab.talk.service.GameRecordService;

@RestController
@RequestMapping("gameRecord")
public class GameRecordController {

	@Autowired
	private GameRecordService gameRecordService;

	// 회원가입 후 전적 생성
	@PostMapping("/createGameRecord/{userId}")
	public ResponseEntity<String> createGameRecord(@RequestBody GameRecordDto gameRecordDto) {
		try {
			gameRecordService.createGameRecord(gameRecordDto);
			return new ResponseEntity<>("전적 생성을 성공했습니다.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("전적 생성을 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//id 별 전적 조회
	@GetMapping("/{userId}")
	public ResponseEntity<GameRecordWithRankDto> findByUserId(@PathVariable("userId") String userId) {
		GameRecordWithRankDto gameRecordWithRankDto = gameRecordService.findByUserId(userId);

		if (gameRecordWithRankDto != null) {
			return new ResponseEntity<>(gameRecordWithRankDto, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//나와 상대의 전적 조회
	@GetMapping("/{userId1}/{userId2}")
	public ResponseEntity<List<GameRecordDto>> findByUserId(@PathVariable("userId1") String userId1,
		@PathVariable("userId2") String userId2) {
		List<GameRecordDto> list = gameRecordService.findMMR(userId1, userId2);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//전체 랭킹 조회
	@GetMapping("/rank")
	public ResponseEntity<List<GameRecordWithRankDto>> showGameRecordList() {
		List<GameRecordWithRankDto> list = gameRecordService.showGameRecordList();

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 게임 후 랭킹

	//게임 후 MMR 갱신 - 게임 구현 후 추가 예정
	@PutMapping("/updateRecord")
	public ResponseEntity<String> updateRecord(@RequestBody PlayerDto playerDto) {
		try {
			gameRecordService.updateRecord(playerDto);
			return new ResponseEntity<>("전적 갱신을 성공했습니다.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("전적 갱신을 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
