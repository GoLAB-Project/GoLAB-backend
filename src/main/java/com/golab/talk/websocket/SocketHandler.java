package com.golab.talk.websocket;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.golab.talk.dto.SocketSendDto;
import com.golab.talk.dto.UserDto;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private Set<WebSocketSession> sessions = new HashSet<>();

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
		SocketSendDto sendDto = objectMapper.readValue(receivedMessage, SocketSendDto.class);

		// message를 파싱하여 처리
		int roomId = Integer.parseInt(sendDto.getRoomId());
		String sendUserId =
			session.getAttributes().get("userId") != null ?
				session.getAttributes().get("userId").toString() : "";
		String receiveUserId = sendDto.getReceiveUserId();
		String content = sendDto.getMessage();

		System.out.println(roomId + " " + sendUserId + " " + receiveUserId + " " + content);

		// 채팅방 update

		for (WebSocketSession currentSession : sessions) {
			if (currentSession == session) {
				// 채팅목록 update

				// update 채팅방 + update 채팅목록 + 메시지
				session.sendMessage(new TextMessage(receivedMessage));
				continue;
			}

			String currentUserId = currentSession.getAttributes().get("userId").toString();

			if (receiveUserId.equals(currentUserId)) {
				// 채팅목록 update

				// update 채팅방 + update 채팅목록 + 메시지
				currentSession.sendMessage(new TextMessage(receivedMessage));
			}
		}
	}

}
