package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.BanDto;

public interface BanService {

	void addBanList(BanDto banDto);

	void deleteBanList(BanDto banDto);

	List<String> showBanList(int myId);

}
