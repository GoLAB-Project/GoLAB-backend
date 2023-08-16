package com.golab.talk.websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

	private Set<WebSocketSession> sessions = new HashSet<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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

		// 원하는 처리 로직을 구현

		// 본인에게 다시 보내기
		//session.sendMessage(new TextMessage(receivedMessage));

		// 현재 연결된 모든 클라이언트에 메시지 전송
		for (WebSocketSession currentSession : sessions) {
			currentSession.sendMessage(new TextMessage(receivedMessage));
		}
	}

}
