package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.repository.KeywordRepository;
import com.golab.talk.service.KeywordService;

@Service
public class KeywordServiceImpl implements KeywordService {

	@Autowired
	private KeywordRepository keywordRepository;

	@Override
	public List<String> findAllKeyword() {
		List<String> list;
		list = keywordRepository.findAllKeyword();
		return list;
	}

}
