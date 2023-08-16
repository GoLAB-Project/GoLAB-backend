package com.golab.talk.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomResponseDto {
	private int roomId;
	private String identifier;
	private String type;
	private String roomName;
	private String lastChat;
	private int notReadChat;
	private int lastReadChatId;
	private LocalDateTime updatedAt;
}
