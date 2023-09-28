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
	public List<TopicDto> showTopicList() {
		List<DiscussionTopic> list = topicRepository.showTopicList();

		return list.stream().map(topic -> new TopicDto(
				topic.getTopicId(),
				topic.getKeyId(),
				topic.getTitle()
			))
			.collect(java.util.stream.Collectors.toList());
	}

	@Override
	public void saveAll(List<TopicDto> list) {
		List<DiscussionTopic> discussionTopic = new ArrayList<>();
		for (TopicDto topicDto : list) {
			discussionTopic.add(new DiscussionTopic(topicDto.getTopicId(), topicDto.getKeyId(), topicDto.getTitle()));
		}
		topicRepository.saveAll(discussionTopic);
	}

	@Override
	public int deleteTopic() {
		List<Integer> topicId = new ArrayList<>();
		List<DiscussionTopic> list = topicRepository.showTopicList();
		for (DiscussionTopic discussionTopic : list) {
			topicId.add(discussionTopic.getTopicId());
		}
		topicRepository.deleteTopic(topicId);

		if (topicRepository.showTopicList().isEmpty()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void resetTopicId() {
		topicRepository.resetTopicId();
	}

}
