package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequestDto {
	private int myId;
	private String type;
	// identifier 제거 - roomId 중심으로 통일
	private String roomName;
	private UserResponseDto[] participant;
}
