package com.golab.talk.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;
import com.golab.talk.dto.ChattingResponseDto;
import com.golab.talk.dto.ParticipantDto;
import com.golab.talk.dto.RoomDto;
import com.golab.talk.dto.RoomListResponseDto;
import com.golab.talk.repository.ChattingRepository;
import com.golab.talk.repository.ParticipantRepository;
import com.golab.talk.repository.RoomRepository;
import com.golab.talk.service.ChattingService;

@Service
public class ChattingServiceImpl implements ChattingService {

	@Autowired
	private ChattingRepository chattingRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private RoomRepository roomRepository;

	public ChattingResponseDto convertToDto(Chatting chatting) {
		return ChattingResponseDto.builder()
			.id(chatting.getId())
			.roomId(chatting.getRoomId())
			.sendUserId(chatting.getSendUserId())
			.message(chatting.getMessage())
			.notRead(chatting.getNotRead())
			.createdAt(chatting.getCreatedAt())
			.build();
	}

	@Override
	public List<ChattingResponseDto> loadChat(int roomId) {
		List<ChattingResponseDto> result = new LinkedList<>();

		Pageable pageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "id"));
		List<Chatting> list = chattingRepository.findByIdLessThanAndRoomIdOrderByIdDesc(50, roomId, pageable);

		for (Chatting chatting : list) {
			result.add(convertToDto(chatting));
		}

		return result;
	}

	@Override
	public List<ParticipantDto> loadRoomData(int userId) {
		List<Participant> list = participantRepository.findRoomDataByUserId(userId);
		List<ParticipantDto> roomData = new LinkedList<>();

		for (Participant p : list) {
			roomData.add(new ParticipantDto(p.getRoomId(), p.getRoomName(), p.getNotReadChat(), p.getLastReadChatId()));
		}

		return roomData;
	}

	@Override
	public List<Integer> getParticipantIdList(int roomId) {
		return participantRepository.getParticipantIdList(roomId);
	}

	@Override
	public RoomDto getRoomDto(int roomId) {
		Room room = roomRepository.findById(roomId);
		RoomDto roomDto = new RoomDto(room.getIdentifier(), room.getType(), room.getLastChat(), room.getUpdatedAt());

		return roomDto;
	}

	@Override
	public List<RoomListResponseDto> getRoomListResponse(int userId, List<ParticipantDto> roomData) {
		List<RoomListResponseDto> response = new LinkedList<>();

		for (ParticipantDto room : roomData) {
			RoomListResponseDto roomListResponseDto = new RoomListResponseDto();

			List<Integer> temp = this.getParticipantIdList(room.getRoomId());
			if (temp.size() == 0) {
				temp.add(userId);
			}
			int[] participant = new int[temp.size()];
			for (int i = 0; i < temp.size(); i++) {
				participant[i] = temp.get(i);
			}

			RoomDto roomInfo = this.getRoomDto(room.getRoomId());

			roomListResponseDto.setRoomId(room.getRoomId());
			roomListResponseDto.setRoomName(room.getRoom_name());
			roomListResponseDto.setType(roomInfo.getType());
			roomListResponseDto.setIdentifier(roomInfo.getIdentifier());
			roomListResponseDto.setParticipant(participant);
			roomListResponseDto.setLastChat(roomInfo.getLast_chat());
			roomListResponseDto.setNotReadChat(room.getNot_read_chat());
			roomListResponseDto.setLastReadChatId(room.getNot_read_chat_id());
			roomListResponseDto.setUpdatedAt(roomInfo.getUpdateAt());

			response.add(roomListResponseDto);
		}

		return response;
	}

	@Override
	public Participant saveParticipant(Participant participant) {
		return participantRepository.save(participant);
	}

}
