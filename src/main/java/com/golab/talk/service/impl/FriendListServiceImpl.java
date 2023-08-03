package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.FriendList;
import com.golab.talk.dto.FriendListDto;
import com.golab.talk.repository.FriendListRepository;
import com.golab.talk.service.FriendListService;

@Service
public class FriendListServiceImpl implements FriendListService {

	@Autowired
	private FriendListRepository friendListRepository;

	@Override
	public void addFriendList(FriendListDto friendListDto) {
		FriendList friendList = new FriendList(friendListDto.getUserId(), friendListDto.getFriendId());
		friendListRepository.save(friendList);
	}

	@Override
	@Transactional
	public void deleteFriendList(FriendListDto friendListDto) {
		friendListRepository.deleteByUserIdAndFriendId(friendListDto.getUserId(), friendListDto.getFriendId());
	}

	@Override
	public List<String> showFriendList(String userId) {
		List<String> list;
		list = friendListRepository.findByUserId(userId);
		return list;
	}

}
