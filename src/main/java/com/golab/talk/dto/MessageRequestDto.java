package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
	private int roomId;
	private String type;
	private UserResponseDto[] participant;
	private String sendUserId;
	private String message;
	private int notRead;
}
