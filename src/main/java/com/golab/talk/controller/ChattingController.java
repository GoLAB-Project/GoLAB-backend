package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.domain.Chatting;
import com.golab.talk.service.ChattingService;

@RestController
@RequestMapping("/chatting")
public class ChattingController {

	@Autowired
	private ChattingService chattingService;

	@GetMapping("/{receiveUserId}")
	public ResponseEntity<List<Chatting>> getChattingList(@PathVariable int receiveUserId) {
		List<Chatting> list = chattingService.getChattingList(receiveUserId);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
