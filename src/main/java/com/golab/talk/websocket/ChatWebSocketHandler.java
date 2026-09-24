package com.golab.talk.websocket;

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
import com.golab.talk.service.ChatService;
import com.golab.talk.service.RoomService;
import com.golab.talk.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

/**
 * 경량화된 WebSocket Handler.
 * JSON 파싱 → DTO 변환 → ChatService 위임 → 응답 전송만 담당한다.
 * 비즈니스 로직은 ChatService 계층에서 처리한다.
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	private ChatService chatService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private WebSocketSessionManager sessionManager;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);

		HttpServletRequest request = (HttpServletRequest) session.getAttributes().get("HTTPSESSION");
		if (request != null) {
			HttpSession httpSession = request.getSession();
			UserDto loggedInUser = (UserDto) httpSession.getAttribute("loggedInUser");
			if (loggedInUser != null) {
				session.getAttributes().put("userId", loggedInUser.getUserId());
			}
		}

		log.debug("WebSocket connected: sessionId={}", session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessionManager.disconnectAll(session);
		log.debug("WebSocket disconnected: sessionId={}", session.getId());
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		SocketReceiveDto receiveDto = objectMapper.readValue(message.getPayload(), SocketReceiveDto.class);

		int sendUserId = getUserId(session);
		String type = receiveDto.getType();

		if ("CHAT".equalsIgnoreCase(type)) {
			handleChat(session, receiveDto, sendUserId);
		} else if ("READ".equalsIgnoreCase(type)) {
			handleRead(session, receiveDto, sendUserId);
		} else {
			log.warn("Unknown event type: {}", type);
		}
	}

	private void handleChat(WebSocketSession session, SocketReceiveDto dto, int sendUserId) throws Exception {
		int roomId = Integer.parseInt(dto.getRoomId());

		// Room에 세션 등록 (최초 입장 시)
		sessionManager.connect(roomId, session);

		// ChatService에 위임 - 저장 + Room.lastChat 갱신
		Chatting saved = chatService.handleChat(roomId, sendUserId, dto.getMessage());
		Room room = roomService.getRoomById(roomId);

		// Room Broadcast
		SocketSendDto sendDto = new SocketSendDto();
		sendDto.setType("CHAT");
		sendDto.setRoomId(roomId);
		sendDto.setChatting(saved);
		sendDto.setRoom(room);

		sessionManager.sendToRoom(roomId, objectMapper.writeValueAsString(sendDto));
	}

	private void handleRead(WebSocketSession session, SocketReceiveDto dto, int userId) {
		int roomId = Integer.parseInt(dto.getRoomId());
		int lastReadChatId = dto.getLastReadChatId() != null ? dto.getLastReadChatId() : 0;

		chatService.handleRead(userId, roomId, lastReadChatId);
		log.debug("READ handled: userId={}, roomId={}, lastReadChatId={}", userId, roomId, lastReadChatId);
	}

	private int getUserId(WebSocketSession session) {
		Object userId = session.getAttributes().get("userId");
		if (userId != null) {
			return Integer.parseInt(userId.toString());
		}
		return 1; // 세션 미구현 시 임시값
	}

}
