package com.golab.talk.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
	private int msgId;
	private int roomId;
	private String sendUserId;
	private String message;
	private int notRead;
	private Date createAt;
}
