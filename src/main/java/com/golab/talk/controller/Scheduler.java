package com.golab.talk.controller;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.service.GameRoomService;
import com.golab.talk.service.TopicService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/scheduler")
public class Scheduler {

	@Autowired
	private GameRoomService gameRoomService;

	@Autowired
	private TopicService topicService;

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

	@Scheduled(cron = "0 50 23 * * *") //매일 23시 50분에 실행
	@Transactional(rollbackOn = Exception.class)
	@DeleteMapping("/topic")
	public ResponseEntity<String> deleteTopic() {
		System.out.println("Topic을 reset합니다." + LocalDateTime.now());
		log.info("Topic을 reset합니다." + LocalDateTime.now());

		if (topicService.deleteTopic() == 1) {
			//게임방 삭제 후 room_id 1로 초기화
			topicService.resetTopicId();
			return new ResponseEntity<>("주제 목록을 삭제하고 topic_id가 1로 초기화 되었습니다.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("주제 목록 초기화를 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
