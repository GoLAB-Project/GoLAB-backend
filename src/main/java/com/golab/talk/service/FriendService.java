package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.FriendDto;

public interface FriendService {

	void addFriendList(FriendDto friendDto);

	void deleteFriendList(FriendDto friendDto);

	List<String> showFriendList(int myId);

}
