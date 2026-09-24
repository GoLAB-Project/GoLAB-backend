package com.golab.talk.service;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Room;

public interface ChatService {

	// 메시지 수신 → 저장 → Room.lastChat 갱신 흐름 조율
	Chatting handleChat(int roomId, int sendUserId, String message);

	// READ 이벤트 처리 - lastReadChatId 갱신
	void handleRead(int userId, int roomId, int lastReadChatId);

	// 1:1 Room 조회 또는 생성
	Room getOrCreateOneToOneRoom(int userAId, int userBId, String roomName);

}
