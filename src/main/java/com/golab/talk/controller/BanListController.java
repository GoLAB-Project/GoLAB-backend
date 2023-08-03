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

import com.golab.talk.dto.BanListDto;
import com.golab.talk.service.BanListService;

@RestController
@RequestMapping("banlist")
public class BanListController {

	@Autowired
	private BanListService banListService;

	@PostMapping
	public ResponseEntity<String> addBanList(@RequestBody BanListDto banListDto) {
		try {
			banListService.addBanList(banListDto);
			return new ResponseEntity<>("친구 차단을 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("친구 차단을 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping
	public ResponseEntity<String> deleteBanList(@RequestBody BanListDto banListDto) {
		try {
			banListService.deleteBanList(banListDto);
			return new ResponseEntity<>("차단 해제를 성공했어요.", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("차단 해제를 실패했어요.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<String>> showBanList(@PathVariable("userId") String userId) {
		List<String> list = banListService.showBanList(userId);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
