package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.BanListDto;

public interface BanListService {

	void addBanList(BanListDto banListDto);

	void deleteBanList(BanListDto banListDto);

	List<String> showBanList(String userId);

}
