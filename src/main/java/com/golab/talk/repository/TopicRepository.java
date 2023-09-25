package com.golab.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.DiscussionTopic;

@Repository
public interface TopicRepository extends JpaRepository<DiscussionTopic, Long> {

}
