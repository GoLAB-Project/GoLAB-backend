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
	private String myId;
	private String type;
	private String identifier;
	private String roomName;
	private UserResponseDto[] participant;
}
