package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.dto.GameRoomDto;
import com.golab.talk.service.GameRecordService;
import com.golab.talk.service.GameRoomService;

@RestController
@RequestMapping("/game-room")
public class GameRoomController {

	@Autowired
	private GameRoomService gameRoomService;

	@Autowired
	private GameRecordService gameRecordService;

	@PostMapping("")
	public ResponseEntity<String> createGameRoom(@RequestBody GameRoomDto gameRoomDto) {
		GameRoomDto room = gameRoomService.createGameRoom(gameRoomDto);
		if (room != null) {
			return new ResponseEntity<>("방을 생성하였습니다.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("방을 생성할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("")
	public ResponseEntity<List<GameRoomDto>> showGameRoomList() {
		List<GameRoomDto> list = gameRoomService.showGameRoomList();

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//방 종류별로 검색
	@GetMapping("/search/type/{gameRoomType}")
	public ResponseEntity<List<GameRoomDto>> showGameRoomListByRoomType(
		@RequestParam("gameRoomType") int gameRoomType) {

		List<GameRoomDto> list = gameRoomService.showGameRoomListByRoomType(gameRoomType);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//이름으로 검색
	@GetMapping("/search/name/{searchKeyword}")
	public ResponseEntity<List<GameRoomDto>> findByGameRoomNameContaining(
		@RequestParam("searchKeyword") String searchKeyword) {

		List<GameRoomDto> list = gameRoomService.findByGameRoomNameContaining(searchKeyword);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//방 번호 검색
	@GetMapping("/search/id/{gameRoomId}")
	public ResponseEntity<GameRoomDto> findByGameRoomId(@RequestParam("gameRoomId") int gameRoomId) {
		GameRoomDto room = gameRoomService.findByGameRoomId(gameRoomId);

		if (room != null) {
			return new ResponseEntity<>(room, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//비밀번호 확인
	@GetMapping("/check/{gameRoomId}")
	public ResponseEntity<String> checkGameRoomPW(@RequestParam("gameRoomId") int gameRoomId,
		@RequestParam("gameRoomPW") String gameRoomPW) {
		GameRoomDto room = gameRoomService.findByGameRoomId(gameRoomId);

		if (room.getGameRoomPW().equals(gameRoomPW)) {
			return new ResponseEntity<>("비밀번호가 일치합니다.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//키워드 가져오기
	@GetMapping("/keyword")
	public ResponseEntity<List<String>> randomKeyword() {
		List<String> list = gameRoomService.randomKeyword();

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//방 상태 변경
	//모두 준비 완료 하면 게임 시작
	@PostMapping("/{gameRoomId}")
	public ResponseEntity<String> updateGameRoomStatus(@RequestBody int roomStatus, int gameRoomId) {
		//게임이 시작했다면

		gameRoomService.updateGameRoomStatus(1, gameRoomId);

		//게임이 끝났다면

		gameRoomService.updateGameRoomStatus(0, gameRoomId);

		return new ResponseEntity<>("success", HttpStatus.OK);
	}

}
