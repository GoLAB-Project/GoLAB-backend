package com.golab.talk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.repository.ParticipantRepository;
import com.golab.talk.repository.RoomRepository;
import com.golab.talk.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Override
	public Room getRoomByIdentifier(String identifier) {
		return roomRepository.findByIdentifier(identifier);
	}

	@Override
	public Participant getRoomInfo(int userId, int roomId) {
		return participantRepository.findByUserIdAndRoomId(userId, roomId);
	}

	@Override
	public Room createRoom(Room room) {
		return roomRepository.save(room);
	}
}
