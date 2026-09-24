package com.golab.talk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.service.ChatService;
import com.golab.talk.service.ChattingService;
import com.golab.talk.service.ParticipantService;
import com.golab.talk.service.RoomService;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	private RoomService roomService;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private ChattingService chattingService;

	@Override
	public Chatting handleChat(int roomId, int sendUserId, String message) {
		// 1. Chatting 저장
		Chatting saved = chattingService.saveChat(roomId, sendUserId, message);

		// 2. Room.lastChat 갱신
		roomService.updateLastChat(roomId, message);

		return saved;
	}

	@Override
	public void handleRead(int userId, int roomId, int lastReadChatId) {
		participantService.updateLastReadChatId(userId, roomId, lastReadChatId);
	}

	@Override
	public Room getOrCreateOneToOneRoom(int userAId, int userBId, String roomName) {
		// 기존 1:1 Room 조회
		Room existing = roomService.getOneToOneRoom(userAId, userBId);
		if (existing != null) {
			return existing;
		}

		// 없으면 생성
		Room room = roomService.createRoom("ONE_TO_ONE");

		// Participant 추가
		participantService.save(new Participant(userAId, room.getId(), roomName));
		participantService.save(new Participant(userBId, room.getId(), roomName));

		return room;
	}

}
