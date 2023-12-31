package com.golab.talk.websocket;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.SocketReceiveDto;
import com.golab.talk.dto.SocketSendDto;
import com.golab.talk.dto.UserDto;
import com.golab.talk.repository.ChattingRepository;
import com.golab.talk.repository.RoomRepository;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private Set<WebSocketSession> sessions = new HashSet<>();

	private RoomRepository roomRepository;

	private ChattingRepository chattingRepository;

	@Autowired
	public SocketHandler(RoomRepository roomRepository, ChattingRepository chattingRepository) {
		this.roomRepository = roomRepository;
		this.chattingRepository = chattingRepository;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 현재 세션에서 값을 불러와 userId 속성 추가
		HttpServletRequest request = (HttpServletRequest)session.getAttributes().get("HTTPSESSION");
		UserDto loggedInUser;
		if (request != null) {
			HttpSession curUserSession = request.getSession();
			loggedInUser = (UserDto)curUserSession.getAttribute("loggedInUser");
			session.getAttributes().put("userId", loggedInUser.getUserId());
		}

		super.afterConnectionEstablished(session);
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessions.remove(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String receivedMessage = message.getPayload();

		ObjectMapper objectMapper = new ObjectMapper();

		SocketReceiveDto receiveDto = objectMapper.readValue(receivedMessage, SocketReceiveDto.class);

		// session과 message를 파싱하여 처리
		// int sendUserId =
		// 	session.getAttributes().get("userId") != null ?
		// 		Integer.parseInt(session.getAttributes().get("userId").toString()) : -1;

		int sendUserId = 1; // <Muk> 임시로 1로 설정
		int receiveUserId = Integer.parseInt(receiveDto.getReceiveUserId());
		String content = receiveDto.getMessage();

		LocalDateTime currentDateTime = LocalDateTime.now();

		String identifier =
			sendUserId < receiveUserId ? sendUserId + "-" + receiveUserId : receiveUserId + "-" + sendUserId;
		int roomId = roomRepository.findByIdentifier(identifier).getId();

		if (receiveDto.getType().equals("chat")) {
			// 채팅방 update
			roomRepository.updateByIdentifier(content, currentDateTime, identifier);

			// 채팅 update
			chattingRepository.insertByRoomId(roomId, sendUserId, content, 1);
		} else {
			// 읽음 처리(receiveUserId는 실제로 보낸 사람의 id(sendUserId)이다.(추후 수정할 예정))
			chattingRepository.updateByRoomId(roomId, receiveUserId, 0);
		}

		for (WebSocketSession currentSession : sessions) {
			if (currentSession == session) {
				objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());

				// 채팅목록 get -> update 채팅방 + update 채팅목록 + 메시지
				List<Room> roomList = roomRepository.getRoomListByUserId(sendUserId);
				List<Chatting> chatList = chattingRepository.getChattingListByRoomId(roomId);
				SocketSendDto sendDto = new SocketSendDto(roomList, chatList);
				session.sendMessage(new TextMessage(objectMapper.writeValueAsString(sendDto)));
				continue;
			}

			/* <Muk> 세션 구현 전까지 막음(채팅방에 있는 유저 중 본인이 아닌 상대방 세션을 찾는 부분)
			int currentUserId = Integer.parseInt(currentSession.getAttributes().get("userId").toString());

			if (receiveUserId == currentUserId) {
				objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());

				// 채팅목록 get -> update 채팅방 + update 채팅목록 + 메시지
				List<Room> roomList = roomRepository.getRoomListByUserId(receiveUserId);
				List<Chatting> chatList = chattingRepository.getChattingListByRoomId(roomId);
				SocketSendDto sendDto = new SocketSendDto(roomList, chatList);
				currentSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(sendDto)));
			} */
		}

	}
}
