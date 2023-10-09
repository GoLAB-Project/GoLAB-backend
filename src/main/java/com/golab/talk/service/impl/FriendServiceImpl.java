package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.Friend;
import com.golab.talk.dto.FriendDto;
import com.golab.talk.repository.FriendRepository;
import com.golab.talk.service.FriendService;

@Service
public class FriendServiceImpl implements FriendService {

	@Autowired
	private FriendRepository friendRepository;

	@Override
	public void addFriendList(FriendDto friendDto) {
		Friend friend = new Friend(friendDto.getMyId(), friendDto.getFriendId(),
			friendDto.getFriendName());
		friendRepository.save(friend);
	}

	@Override
	@Transactional
	public void deleteFriendList(FriendDto friendDto) {
		friendRepository.deleteByMyIdAndFriendId(friendDto.getMyId(), friendDto.getFriendId());
	}

	@Override
	public List<String> showFriendList(int myId) {
		List<String> list;
		list = friendRepository.findByMyId(myId);
		return list;
	}

}
