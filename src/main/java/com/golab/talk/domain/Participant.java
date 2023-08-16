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
@Table(name = "PARTICIPANT")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private int id;

	@Column(name = "USER_ID", nullable = false)
	private int userId;
	@Column(name = "ROOM_ID", nullable = false)
	private int roomId;
	@Column(name = "ROOM_NAME", nullable = false)
	private String roomName;
	@Column(name = "NOT_READ_CHAT", nullable = false)
	private int notReadChat;
	@Column(name = "LAST_READ_CHAT_ID", nullable = false)
	private int lastReadChatId;
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;
	@Column(name = "UPDATED_AT", nullable = false)
	private LocalDateTime updatedAt;

}
