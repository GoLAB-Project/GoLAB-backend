package com.golab.talk.service;

import java.util.List;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.RoomListResponseDto;

public interface RoomService {

	Room getRoomById(int roomId);

	// 1:1 Room 조회 - Participant 관계 기반
	Room getOneToOneRoom(int userAId, int userBId);

	Room createRoom(String type);

	int updateLastChat(int roomId, String lastChat);

	List<RoomListResponseDto> getRoomList(int userId);

	Participant getRoomInfo(int userId, int roomId);

}
