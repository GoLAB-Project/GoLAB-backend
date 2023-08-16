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
@Table(name = "FRIEND")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "MY_ID", nullable = false)
	private int myId;
	@Column(name = "FRIEND_ID", nullable = false)
	private int friendId;
	@Column(name = "FRIEND_NAME", nullable = false)
	private String friendName;
	// @Column(name = "CREATED_AT", nullable = false)
	// private LocalDateTime createdAt;
	// @Column(name = "UPDATED_AT", nullable = false)
	// private LocalDateTime updatedAt;

	public Friend(int myId, int friendId, String friendName) {
		this.myId = myId;
		this.friendId = friendId;
		this.friendName = friendName;
	}

}
