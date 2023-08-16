package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.Ban;
import com.golab.talk.dto.BanDto;
import com.golab.talk.repository.BanRepository;
import com.golab.talk.service.BanService;

@Service
public class BanServiceImpl implements BanService {

	@Autowired
	private BanRepository banRepository;

	@Override
	public void addBanList(BanDto banDto) {
		Ban ban = new Ban(banDto.getMyId(), banDto.getBanId(), banDto.getBanName());
		banRepository.save(ban);
	}

	@Override
	@Transactional
	public void deleteBanList(BanDto banDto) {
		banRepository.deleteByMyIdAndBanId(banDto.getMyId(), banDto.getBanId());
	}

	@Override
	public List<String> showBanList(int myId) {
		List<String> list;
		list = banRepository.findByMyId(myId);
		return list;
	}

}
