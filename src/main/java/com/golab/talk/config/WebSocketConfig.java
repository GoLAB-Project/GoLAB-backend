package com.golab.talk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.golab.talk.websocket.ChatWebSocketHandler;
import com.golab.talk.websocket.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private ChatWebSocketHandler chatWebSocketHandler;

	// 기존 SocketHandler는 레거시로 유지
	@Autowired
	private SocketHandler socketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 신규: roomId 기반 채팅 핸들러
		registry.addHandler(chatWebSocketHandler, "/ws/chat").setAllowedOrigins("*");
		// 레거시: 기존 핸들러 유지
		registry.addHandler(socketHandler, "/socket").setAllowedOrigins("*");
	}

}
