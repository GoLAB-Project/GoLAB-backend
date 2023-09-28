package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.TopicDto;

public interface TopicService {

	List<TopicDto> showTopicList();

	void saveAll(List<TopicDto> list);

	int deleteTopic();

	void resetTopicId();

}
