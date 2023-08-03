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

import com.golab.talk.dto.FriendListDto;
import com.golab.talk.service.FriendListService;

@RestController
@RequestMapping("/friendlist")
public class FriendListController {

	@Autowired
	private FriendListService friendListService;

	@PostMapping
	public ResponseEntity<String> addFriendList(@RequestBody FriendListDto friendListDto) {
		try {
			friendListService.addFriendList(friendListDto);
			return new ResponseEntity<>("친구 추가를 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("친구 추가를 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<String> deleteFriendList(@RequestBody FriendListDto friendListDto) {
		try {
			friendListService.deleteFriendList(friendListDto);
			return new ResponseEntity<>("친구 삭제를 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("친구 삭제를 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<String>> showFriendList(@PathVariable("userId") String userId) {
		List<String> list = friendListService.showFriendList(userId);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
