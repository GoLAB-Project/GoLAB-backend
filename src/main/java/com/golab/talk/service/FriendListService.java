package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.FriendListDto;

public interface FriendListService {

	void addFriendList(FriendListDto friendListDto);

	void deleteFriendList(FriendListDto friendListDto);

	List<String> showFriendList(String userId);

}
