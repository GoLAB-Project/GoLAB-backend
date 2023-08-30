package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomDto {

	private int gameRoomId;
	private int gameRoomType;
	private int maxParticipants;
	private String gameRoomName;
	private String gameRoomPW;
	private int roomStatus;

}
