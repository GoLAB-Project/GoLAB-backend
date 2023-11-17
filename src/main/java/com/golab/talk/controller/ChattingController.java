package com.golab.talk.controller;

import java.util.List;

import com.golab.talk.dto.ChattingListDto;
import com.golab.talk.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.domain.Chatting;
import com.golab.talk.service.ChattingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/chatting")
public class ChattingController {

	@Autowired
	private ChattingService chattingService;

	@GetMapping("/{receiveUserId}")
	public ResponseEntity<List<Chatting>> getChattingList(@PathVariable int receiveUserId, HttpServletRequest request) {
		List<Chatting> list = chattingService.getChattingList(receiveUserId, request);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//범석 추가
	// 왼쪽 채팅목록들 가져오기..
	@GetMapping("/")
	public ResponseEntity<List<ChattingListDto>> getList(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session.getAttribute("loggedInUser")!=null) {
			int id = ((UserDto) session.getAttribute("loggedInUser")).getId();
			List<ChattingListDto> list = chattingService.getRoomList(id);
			System.out.println(list.get(0).getRoom_id());
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
