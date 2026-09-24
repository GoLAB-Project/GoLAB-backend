package com.golab.talk.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

/**
 * Room 기반 WebSocket 세션 관리.
 * roomId → Set<WebSocketSession> 구조로 세션을 관리하고 Room 단위 Broadcast를 수행한다.
 */
@Slf4j
@Component
public class WebSocketSessionManager {

	// roomId → 해당 Room에 연결된 세션 집합
	private final Map<Integer, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

	public void connect(int roomId, WebSocketSession session) {
		roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
		log.debug("Session connected: roomId={}, sessionId={}", roomId, session.getId());
	}

	public void disconnect(int roomId, WebSocketSession session) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions != null) {
			sessions.remove(session);
			if (sessions.isEmpty()) {
				roomSessions.remove(roomId);
			}
		}
		log.debug("Session disconnected: roomId={}, sessionId={}", roomId, session.getId());
	}

	public void disconnectAll(WebSocketSession session) {
		roomSessions.forEach((roomId, sessions) -> sessions.remove(session));
	}

	/** Room 내 모든 세션에 메시지 Broadcast */
	public void sendToRoom(int roomId, String payload) {
		Set<WebSocketSession> sessions = roomSessions.get(roomId);
		if (sessions == null) return;

		TextMessage message = new TextMessage(payload);
		for (WebSocketSession session : sessions) {
			if (session.isOpen()) {
				try {
					session.sendMessage(message);
				} catch (IOException e) {
					log.warn("Failed to send message to session={}: {}", session.getId(), e.getMessage());
				}
			}
		}
	}

	/** 특정 세션에만 메시지 전송 */
	public void sendToSession(WebSocketSession session, String payload) {
		if (session.isOpen()) {
			try {
				session.sendMessage(new TextMessage(payload));
			} catch (IOException e) {
				log.warn("Failed to send message to session={}: {}", session.getId(), e.getMessage());
			}
		}
	}

}
