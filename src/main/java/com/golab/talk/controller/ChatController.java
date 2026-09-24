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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.ChattingPageResponse;
import com.golab.talk.dto.CreateRoomRequestDto;
import com.golab.talk.dto.CreateRoomResponseDto;
import com.golab.talk.dto.RoomListResponseDto;
import com.golab.talk.dto.UserResponseDto;
import com.golab.talk.service.ChatService;
import com.golab.talk.service.ChattingService;
import com.golab.talk.service.ParticipantService;
import com.golab.talk.service.RoomService;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private ChattingService chattingService;

	/** 채팅방 목록 조회 */
	@GetMapping("/rooms")
	public ResponseEntity<List<RoomListResponseDto>> getRoomList(
		@RequestParam(required = false) Integer userId,
		javax.servlet.http.HttpServletRequest request
	) {
		int actualUserId = resolveUserId(userId, request);
		List<RoomListResponseDto> list = roomService.getRoomList(actualUserId);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/** 채팅방 생성 또는 기존 방 반환 */
	@PostMapping("/rooms")
	public ResponseEntity<CreateRoomResponseDto> createRoom(
		@RequestBody CreateRoomRequestDto request,
		javax.servlet.http.HttpServletRequest httpRequest
	) {
		int myId = request.getMyId() > 0 ? request.getMyId() : resolveUserId(null, httpRequest);
		String roomName = request.getRoomName();
		UserResponseDto[] participants = request.getParticipant();
		int otherUserId = (participants != null && participants.length > 0) ? participants[0].getId() : myId;

		Room room = chatService.getOrCreateOneToOneRoom(myId, otherUserId, roomName);
		Participant myInfo = roomService.getRoomInfo(myId, room.getId());

		CreateRoomResponseDto response = new CreateRoomResponseDto();
		response.setRoomId(room.getId());
		response.setType(room.getType());
		response.setRoomName(roomName);
		response.setLastChat(room.getLastChat());
		response.setNotReadChat(myInfo != null ? myInfo.getNotReadChat() : 0);
		response.setLastReadChatId(myInfo != null ? myInfo.getLastReadChatId() : 0);
		response.setUpdatedAt(room.getUpdatedAt());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/** 채팅 이력 조회 (Cursor Pagination) */
	@GetMapping("/rooms/{roomId}/messages")
	public ResponseEntity<ChattingPageResponse> getMessages(
		@PathVariable int roomId,
		@RequestParam(defaultValue = "2147483647") int cursor,
		@RequestParam(defaultValue = "30") int size
	) {
		ChattingPageResponse response = chattingService.getChattingPage(roomId, cursor, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/** 읽음 처리 */
	@PostMapping("/rooms/{roomId}/read")
	public ResponseEntity<Void> readChat(
		@PathVariable int roomId,
		@RequestParam(required = false) Integer userId,
		@RequestParam int lastReadChatId,
		javax.servlet.http.HttpServletRequest request
	) {
		int actualUserId = resolveUserId(userId, request);
		chatService.handleRead(actualUserId, roomId, lastReadChatId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private int resolveUserId(Integer userId, javax.servlet.http.HttpServletRequest request) {
		if (userId != null && userId > 0) {
			return userId;
		}
		if (request != null) {
			javax.servlet.http.HttpSession session = request.getSession(false);
			if (session != null) {
				Object loggedInUser = session.getAttribute("loggedInUser");
				if (loggedInUser instanceof com.golab.talk.dto.UserDto) {
					// 세션에는 login ID가 들어있을 수 있으므로 필요한 경우 fallback
				}
			}
		}
		return 1;
	}

}
