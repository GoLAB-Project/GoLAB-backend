package com.golab.talk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.DiscussionTopic;
import com.golab.talk.dto.TopicDto;
import com.golab.talk.repository.TopicRepository;
import com.golab.talk.service.TopicService;

@Service
public class TopicServiceImpl implements TopicService {

	@Autowired
	private TopicRepository topicRepository;

	@Override
	public void saveAll(List<TopicDto> list) {
		List<DiscussionTopic> discussionTopic = new ArrayList<>();
		for (TopicDto topicDto : list) {
			discussionTopic.add(new DiscussionTopic(topicDto.getTopicId(), topicDto.getKeyId(), topicDto.getTitle()));
		}
		topicRepository.saveAll(discussionTopic);
	}

}
