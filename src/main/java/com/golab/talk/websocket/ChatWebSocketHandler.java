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

		int sendUserId = getUserId(session, receiveDto);
		String type = receiveDto.getType();

		if ("CHAT".equalsIgnoreCase(type)) {
			handleChat(session, receiveDto, sendUserId);
		} else if ("READ".equalsIgnoreCase(type)) {
			handleRead(session, receiveDto, sendUserId);
		} else if ("JOIN".equalsIgnoreCase(type) || "ENTER".equalsIgnoreCase(type)) {
			handleJoin(session, receiveDto);
		} else {
			log.warn("Unknown event type: {}", type);
		}
	}

	private void handleJoin(WebSocketSession session, SocketReceiveDto dto) {
		if (dto.getRoomId() != null && !dto.getRoomId().isEmpty()) {
			int roomId = Integer.parseInt(dto.getRoomId());
			sessionManager.connect(roomId, session);
			log.debug("Session joined room: roomId={}, sessionId={}", roomId, session.getId());
		}
	}

	private void handleChat(WebSocketSession session, SocketReceiveDto dto, int sendUserId) throws Exception {
		int roomId = Integer.parseInt(dto.getRoomId());

		// Room에 세션 등록 (최초 입장 시)
		sessionManager.connect(roomId, session);

		// ChatService에 위임 - 저장 + Room.lastChat 갱신
		Chatting saved = chatService.handleChat(roomId, sendUserId, dto.getMessage());
		// 발신자 본인은 본인이 보낸 메시지를 읽은 것으로 처리
		chatService.handleRead(sendUserId, roomId, saved.getId());
		Room room = roomService.getRoomById(roomId);

		// Room Broadcast
		SocketSendDto sendDto = new SocketSendDto();
		sendDto.setType("CHAT");
		sendDto.setRoomId(roomId);
		sendDto.setChatting(saved);
		sendDto.setRoom(room);

		sessionManager.sendToRoom(roomId, objectMapper.writeValueAsString(sendDto));
	}

	private void handleRead(WebSocketSession session, SocketReceiveDto dto, int userId) throws Exception {
		int roomId = Integer.parseInt(dto.getRoomId());
		int lastReadChatId = dto.getLastReadChatId() != null ? dto.getLastReadChatId() : 0;

		chatService.handleRead(userId, roomId, lastReadChatId);
		log.debug("READ handled: userId={}, roomId={}, lastReadChatId={}", userId, roomId, lastReadChatId);

		// 읽음 상태 방 내 다른 참여자들에게 Broadcast
		SocketSendDto sendDto = new SocketSendDto();
		sendDto.setType("READ");
		sendDto.setRoomId(roomId);
		sendDto.setUserId(userId);
		sendDto.setLastReadChatId(lastReadChatId);
		sessionManager.sendToRoom(roomId, objectMapper.writeValueAsString(sendDto));
	}

	private int getUserId(WebSocketSession session, SocketReceiveDto dto) {
		if (dto != null && dto.getSendUserId() != null && dto.getSendUserId() > 0) {
			return dto.getSendUserId();
		}
		Object userId = session.getAttributes().get("userId");
		if (userId != null) {
			try {
				return Integer.parseInt(userId.toString());
			} catch (NumberFormatException ignored) {}
		}
		if (session.getUri() != null && session.getUri().getQuery() != null) {
			String query = session.getUri().getQuery();
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				if (pair.length == 2 && "userId".equalsIgnoreCase(pair[0])) {
					try {
						return Integer.parseInt(pair[1]);
					} catch (NumberFormatException ignored) {}
				}
			}
		}
		return 1; // 세션 미구현 시 기본값
	}

}
