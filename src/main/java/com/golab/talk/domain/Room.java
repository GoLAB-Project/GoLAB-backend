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
import lombok.ToString;

@Entity
@Table(name = "ROOM")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@Column(name = "identifier", nullable = false)
	private String identifier;
	@Column(name = "type", nullable = false)
	private String type;
	@Column(name = "last_chat", columnDefinition = "text")
	private String lastChat;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public Room(String type, String identifier, String lastChat) {
		this.type = type;
		this.identifier = identifier;
		this.lastChat = lastChat;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

}
