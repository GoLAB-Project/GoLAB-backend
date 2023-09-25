package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.KeywordDto;

public interface KeywordService {

	//모든 키워드 조회
	List<KeywordDto> findAll();

}
