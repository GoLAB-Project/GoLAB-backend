package com.golab.talk.service;

import java.util.List;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Participant;
import com.golab.talk.dto.ChattingResponseDto;
import com.golab.talk.dto.ParticipantDto;
import com.golab.talk.dto.RoomDto;
import com.golab.talk.dto.RoomListResponseDto;

public interface ChattingService {

	List<ChattingResponseDto> loadChat(int roomId);

	List<ParticipantDto> loadRoomData(int userId);

	List<Integer> getParticipantIdList(int roomId);

	RoomDto getRoomDto(int roomId);

	List<RoomListResponseDto> getRoomListResponse(int userId, List<ParticipantDto> roomData);

	Participant saveParticipant(Participant participant);

	List<Chatting> getChattingList(int receiveUserId);

}
