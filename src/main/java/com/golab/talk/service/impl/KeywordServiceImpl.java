package com.golab.talk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.Keyword;
import com.golab.talk.dto.KeywordDto;
import com.golab.talk.repository.KeywordRepository;
import com.golab.talk.service.KeywordService;

@Service
public class KeywordServiceImpl implements KeywordService {

	@Autowired
	private KeywordRepository keywordRepository;

	@Override
	public List<KeywordDto> findAll() {
		List<Keyword> list = keywordRepository.findAll();
		return list.stream().map(keyword -> new KeywordDto(keyword.getKeyId(), keyword.getKeyName()))
			.collect(java.util.stream.Collectors.toList());

	}

}
