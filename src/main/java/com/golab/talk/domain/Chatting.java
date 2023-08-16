package com.golab.talk.domain;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "CHATTING")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chatting {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "ROOM_ID", nullable = false)
	private int roomId;

	@Column(name = "SEND_USER_ID", nullable = false)
	private int sendUserId;

	@Column(name = "MESSAGE", nullable = false)
	private String message;

	@Column(name = "NOT_READ", nullable = false)
	private int notRead;

	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "UPDATED_AT", nullable = false)
	private LocalDateTime updatedAt;
	
}
