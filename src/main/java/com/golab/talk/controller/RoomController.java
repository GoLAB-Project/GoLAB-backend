package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.ChattingPageResponse;
import com.golab.talk.dto.RoomListResponseDto;
import com.golab.talk.service.ChattingService;
import com.golab.talk.service.RoomService;

/**
 * 레거시 RoomController - /room 경로 유지.
 * 신규 API는 ChatController(/chat)를 사용한다.
 */
@RestController
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private ChattingService chattingService;

	@Autowired
	private RoomService roomService;

	@GetMapping("/{roomId}")
	public ResponseEntity<ChattingPageResponse> loadChat(@PathVariable int roomId) {
		ChattingPageResponse page = chattingService.getChattingPage(roomId, Integer.MAX_VALUE, 50);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@GetMapping("/list/{userId}")
	public ResponseEntity<List<RoomListResponseDto>> loadRoomList(@PathVariable int userId) {
		List<RoomListResponseDto> response = roomService.getRoomList(userId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
