package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Participant;
import com.golab.talk.repository.ParticipantRepository;
import com.golab.talk.service.ParticipantService;

@Service
public class ParticipantServiceImpl implements ParticipantService {

	@Autowired
	private ParticipantRepository participantRepository;

	@Override
	public Participant findByUserIdAndRoomId(int userId, int roomId) {
		return participantRepository.findByUserIdAndRoomId(userId, roomId);
	}

	@Override
	public List<Participant> findByRoomId(int roomId) {
		return participantRepository.findByRoomId(roomId);
	}

	@Override
	public List<Integer> getParticipantIdList(int roomId) {
		return participantRepository.getParticipantIdList(roomId);
	}

	@Override
	public Participant save(Participant participant) {
		return participantRepository.save(participant);
	}

	@Override
	public void updateLastReadChatId(int userId, int roomId, int lastReadChatId) {
		participantRepository.updateLastReadChatId(userId, roomId, lastReadChatId);
	}

}
