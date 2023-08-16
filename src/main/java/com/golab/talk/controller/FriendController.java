package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.FriendDto;
import com.golab.talk.service.FriendService;

@RestController
@RequestMapping("/friend")
public class FriendController {

	@Autowired
	private FriendService friendService;

	@PostMapping
	public ResponseEntity<String> addFriendList(@RequestBody FriendDto friendDto) {
		try {
			friendService.addFriendList(friendDto);
			return new ResponseEntity<>("친구 추가를 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("친구 추가를 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<String> deleteFriendList(@RequestBody FriendDto friendDto) {
		try {
			friendService.deleteFriendList(friendDto);
			return new ResponseEntity<>("친구 삭제를 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("친구 삭제를 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{myId}")
	public ResponseEntity<List<String>> showFriendList(@PathVariable("myId") int myId) {
		List<String> list = friendService.showFriendList(myId);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
