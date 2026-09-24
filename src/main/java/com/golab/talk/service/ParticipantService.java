package com.golab.talk.service;

import java.util.List;

import com.golab.talk.domain.Participant;

public interface ParticipantService {

	Participant findByUserIdAndRoomId(int userId, int roomId);

	List<Participant> findByRoomId(int roomId);

	List<Integer> getParticipantIdList(int roomId);

	Participant save(Participant participant);

	void updateLastReadChatId(int userId, int roomId, int lastReadChatId);

}
