package com.golab.talk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.ChattingPageResponse;
import com.golab.talk.service.ChattingService;

/**
 * 레거시 ChattingController - /chatting 경로 유지.
 * 신규 API는 ChatController(/chat)를 사용한다.
 */
@RestController
@RequestMapping("/chatting")
public class ChattingController {

	@Autowired
	private ChattingService chattingService;

	@GetMapping("/{roomId}")
	public ResponseEntity<ChattingPageResponse> getChattingList(@PathVariable int roomId) {
		ChattingPageResponse page = chattingService.getChattingPage(roomId, Integer.MAX_VALUE, 50);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

}
