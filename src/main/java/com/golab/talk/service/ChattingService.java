package com.golab.talk.service;

import java.util.List;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Participant;
import com.golab.talk.dto.*;

import javax.servlet.http.HttpServletRequest;

public interface ChattingService {

	List<ChattingResponseDto> loadChat(int roomId);

	List<ParticipantDto> loadRoomData(int userId);

	List<Integer> getParticipantIdList(int roomId);

	RoomDto getRoomDto(int roomId);

	List<RoomListResponseDto> getRoomListResponse(int userId, List<ParticipantDto> roomData);

	Participant saveParticipant(Participant participant);

	List<Chatting> getChattingList(int receiveUserId, HttpServletRequest request);

	//범석추가
	List<ChattingListDto> getRoomList(int userId);

}
