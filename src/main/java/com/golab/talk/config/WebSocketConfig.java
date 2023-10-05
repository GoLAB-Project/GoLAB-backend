package com.golab.talk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.golab.talk.repository.ChattingRepository;
import com.golab.talk.repository.RoomRepository;
import com.golab.talk.websocket.SocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private ChattingRepository chattingRepository;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SocketHandler(roomRepository, chattingRepository), "/socket").setAllowedOrigins("*");
	}

}
