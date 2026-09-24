package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketReceiveDto {
	private String type;       // CHAT | READ | ROOM_UPDATE | JOIN
	private String roomId;
	private Integer sendUserId;
	// receiveUserId 제거 - roomId 중심으로 통일
	private String message;
	private Integer lastReadChatId; // READ 이벤트 시 사용
}
