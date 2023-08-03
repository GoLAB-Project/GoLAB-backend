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
@Table(name = "MESSAGE")
@Getter
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MSG_ID", nullable = false)
	private int msgId;

	@Column(name = "ROOM_ID", nullable = false)
	private int roomId;
	@Column(name = "SENDER", nullable = false)
	private String sender;
	@Column(name = "RECEIVER", nullable = false)
	private String receiver;
	@Column(name = "MSG_TIME", nullable = false)
	private String msgTime;
	@Column(name = "MSG_CONTENT", nullable = false)
	private String msgContent;
	@Column(name = "MSG_TYPE", nullable = false)
	private int msgType;
	@Column(name = "IS_READ", nullable = false)
	private boolean isRead;
}
