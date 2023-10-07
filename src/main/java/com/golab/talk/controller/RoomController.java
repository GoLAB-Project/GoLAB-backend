package com.golab.talk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.ChattingResponseDto;
import com.golab.talk.dto.CreateRoomRequestDto;
import com.golab.talk.dto.CreateRoomResponseDto;
import com.golab.talk.dto.ParticipantDto;
import com.golab.talk.dto.RoomListResponseDto;
import com.golab.talk.dto.UserResponseDto;
import com.golab.talk.service.ChattingService;
import com.golab.talk.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private ChattingService chattingService;

	@Autowired
	private RoomService roomService;

	@PostMapping("/create")
	public ResponseEntity<CreateRoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
		CreateRoomResponseDto data = new CreateRoomResponseDto();

		int myId = createRoomRequestDto.getMyId();
		String type = createRoomRequestDto.getType();
		String roomName = createRoomRequestDto.getRoomName();
		String identifier = createRoomRequestDto.getIdentifier();
		UserResponseDto[] participant = createRoomRequestDto.getParticipant();

		Room findRoom = roomService.getRoomByIdentifier(identifier);
		if (findRoom != null) {
			Participant findRoomInfo = roomService.getRoomInfo(myId, findRoom.getId());

			data.setRoomId(findRoom.getId());
			data.setIdentifier(findRoom.getIdentifier());
			data.setType(findRoom.getType());
			data.setRoomName(findRoomInfo.getRoomName());
			data.setNotReadChat(findRoomInfo.getNotReadChat());
			data.setLastReadChatId(findRoomInfo.getLastReadChatId());
			data.setLastChat(findRoom.getLastChat());
			data.setUpdatedAt(findRoom.getUpdatedAt());
		} else {
			Room room = roomService.createRoom(new Room(type, identifier, ""));

			for (UserResponseDto user : participant) {
				Participant saveUser = new Participant(user.getId(), room.getId(), roomName);
				chattingService.saveParticipant(saveUser);
			}

			data.setRoomId(room.getId());
			data.setIdentifier(room.getIdentifier());
			data.setType(room.getType());
			data.setRoomName(roomName);
			data.setNotReadChat(0);
			data.setLastReadChatId(0);
			data.setLastChat(room.getLastChat());
			data.setUpdatedAt(room.getUpdatedAt());
		}

		if (data != null) {
			return new ResponseEntity<>(data, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<List<ChattingResponseDto>> loadChat(@PathVariable int roomId) {
		List<ChattingResponseDto> list = chattingService.loadChat(roomId);

		if (list != null) {
			return new ResponseEntity<>(list, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/list/{userId}")
	public ResponseEntity<List<RoomListResponseDto>> loadRoomList(@PathVariable int userId) {
		List<ParticipantDto> roomData = chattingService.loadRoomData(userId);

		List<RoomListResponseDto> response = chattingService.getRoomListResponse(userId, roomData);

		if (response != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
