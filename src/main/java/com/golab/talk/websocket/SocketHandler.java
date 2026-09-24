package com.golab.talk.websocket;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.golab.talk.dto.UserDto;

/**
 * 레거시 SocketHandler - /socket 경로 유지.
 * 신규 채팅은 ChatWebSocketHandler(/ws/chat)를 사용한다.
 */
@Component
public class SocketHandler extends TextWebSocketHandler {

	private Set<WebSocketSession> sessions = new HashSet<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		HttpServletRequest request = (HttpServletRequest) session.getAttributes().get("HTTPSESSION");
		if (request != null) {
			HttpSession curUserSession = request.getSession();
			UserDto loggedInUser = (UserDto) curUserSession.getAttribute("loggedInUser");
			if (loggedInUser != null) {
				session.getAttributes().put("userId", loggedInUser.getUserId());
			}
		}
		super.afterConnectionEstablished(session);
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		sessions.remove(session);
	}

}
