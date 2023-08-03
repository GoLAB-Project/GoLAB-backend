package com.golab.talk.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "DISCUSSION_TOPIC")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionTopic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOPIC_ID", nullable = false)
	private int topicId;

	@Column(name = "KEY_ID", nullable = false)
	private int keyId;
	@Column(name = "TITLE", nullable = false)
	private String title;
}
