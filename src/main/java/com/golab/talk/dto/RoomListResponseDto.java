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
public class RoomListResponseDto {
	private int roomId;
	private String type;
	private String identifier;
	private String roomName;
	private String[] participant;
	private String lastChat;
	private int notReadChat;
	private String lastReadChatId;
	private Date createAt;
}
