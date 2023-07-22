package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.BanList;
import com.golab.talk.dto.BanListDto;
import com.golab.talk.repository.BanListRepository;
import com.golab.talk.service.BanListService;

@Service
public class BanListServiceImpl implements BanListService {

	@Autowired
	private BanListRepository banListRepository;

	@Override
	public void addBanList(BanListDto banListDto) {
		BanList banList = new BanList(banListDto.getUserId(), banListDto.getBanId());
		banListRepository.save(banList);
	}

	@Override
	@Transactional
	public void deleteBanList(BanListDto banListDto) {
		banListRepository.deleteByUserIdAndBanId(banListDto.getUserId(), banListDto.getBanId());
	}

	@Override
	public List<String> showBanList(String userId) {
		List<String> list;
		list = banListRepository.findByUserId(userId);
		return list;
	}

}
