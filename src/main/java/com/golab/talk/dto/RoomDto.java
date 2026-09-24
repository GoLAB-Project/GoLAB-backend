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
public class RoomDto {
	// identifier 제거 - roomId 중심으로 통일
	private int roomId;
	private String type;
	private String lastChat;
	private LocalDateTime updatedAt;
}
