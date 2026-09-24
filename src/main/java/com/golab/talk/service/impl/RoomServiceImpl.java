package com.golab.talk.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.RoomListResponseDto;
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
	public Room getRoomById(int roomId) {
		return roomRepository.findById(roomId);
	}

	@Override
	public Room getOneToOneRoom(int userAId, int userBId) {
		Integer roomId = participantRepository.findOneToOneRoomId(userAId, userBId);
		if (roomId == null) {
			return null;
		}
		return roomRepository.findById((int) roomId);
	}

	@Override
	public Room createRoom(String type) {
		Room room = new Room(type, "");
		return roomRepository.save(room);
	}

	@Override
	public int updateLastChat(int roomId, String lastChat) {
		return roomRepository.updateById(lastChat, java.time.LocalDateTime.now(), roomId);
	}

	@Override
	public List<RoomListResponseDto> getRoomList(int userId) {
		List<Participant> participants = participantRepository.findRoomDataByUserId(userId);
		List<RoomListResponseDto> result = new LinkedList<>();

		for (Participant p : participants) {
			Room room = roomRepository.findById(p.getRoomId());
			if (room == null) continue;

			List<Integer> participantIds = participantRepository.getParticipantIdList(p.getRoomId());
			int[] participantArr = participantIds.stream().mapToInt(Integer::intValue).toArray();

			RoomListResponseDto dto = new RoomListResponseDto();
			dto.setRoomId(p.getRoomId());
			dto.setType(room.getType());
			dto.setRoomName(p.getRoomName());
			dto.setParticipant(participantArr);
			dto.setLastChat(room.getLastChat());
			dto.setNotReadChat(p.getNotReadChat());
			dto.setLastReadChatId(p.getLastReadChatId());
			dto.setUpdatedAt(room.getUpdatedAt());

			result.add(dto);
		}

		return result;
	}

	@Override
	public Participant getRoomInfo(int userId, int roomId) {
		return participantRepository.findByUserIdAndRoomId(userId, roomId);
	}

}
