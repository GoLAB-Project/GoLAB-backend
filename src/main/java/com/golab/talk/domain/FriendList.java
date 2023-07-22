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
@Table(name = "FRIEND_LIST")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FRIEND_IDX", nullable = false)
	private int friendIdx;

	@Column(name = "USER_ID", nullable = false)
	private String userId;
	@Column(name = "FRIEND_ID", nullable = false)
	private String friendId;

}
