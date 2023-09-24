package com.golab.talk.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.service.GameRoomService;
import com.golab.talk.service.KeywordService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/scheduler")
public class Scheduler {

	@Autowired
	private GameRoomService gameRoomService;

	@Autowired
	private KeywordService keywordService;

	@Scheduled(cron = "0 50 23 * * *") //매일 23시 50분에 실행
	@Transactional(rollbackOn = Exception.class)
	@DeleteMapping("/gameRoom")
	public ResponseEntity<String> deleteGameRoom() {
		System.out.println("GameRoom을 reset합니다." + LocalDateTime.now());
		log.info("GameRoom을 reset합니다." + LocalDateTime.now());

		if (gameRoomService.deleteGameRoom() == 1) {
			//게임방 삭제 후 room_id 1로 초기화
			gameRoomService.resetGameRoomId();
			return new ResponseEntity<>("게임방 목록을 삭제하고 room_id가 1로 초기화 되었습니다.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("게임방 목록 초기화를 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@Scheduled(cron = "0 50 23 * * *") //매일 23시 50분에 실행
	@GetMapping("/keyword")
	public ResponseEntity<List<String>> getTodayTopic() {
		System.out.println("오늘의 키워드를 생성합니다." + LocalDateTime.now());
		log.info("오늘의 키워드를 생성합니다." + LocalDateTime.now());

		List<String> list = keywordService.findAllKeyword();

		for (String keyword : list) {
			String question = "우리가 대한민국 10대들을 대상으로 1~3분 정도 되는 토론 게임을 하려고 해. 그 때 \""
				+ keyword + "\"에 대한 짧은 주제를 10가지 제시해줘.";
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

}
