package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketSendDto {
	private String roomId;
	private String receiveUserId;
	private String message;
}
