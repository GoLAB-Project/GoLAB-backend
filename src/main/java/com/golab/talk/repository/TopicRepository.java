package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.DiscussionTopic;

@Repository
public interface TopicRepository extends JpaRepository<DiscussionTopic, Long> {

	// 게임방 리스트
	@Query(value = "SELECT * FROM discussion_topic", nativeQuery = true)
	List<DiscussionTopic> showTopicList();

	//토픽 초기화
	@Modifying
	@Query(value = "DELETE FROM discussion_topic A WHERE A.topic_id in :topicId", nativeQuery = true)
	void deleteTopic(@Param("topicId") List<Integer> topicId);

	//토픽 번호 초기화
	@Modifying
	@Query(value = "ALTER TABLE discussion_topic AUTO_INCREMENT = 1", nativeQuery = true)
	void resetTopicId();
}
