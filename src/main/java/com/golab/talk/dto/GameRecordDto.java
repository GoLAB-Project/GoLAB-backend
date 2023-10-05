package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
<<<<<<<< HEAD:src/main/java/com/golab/talk/dto/GameRecordDto.java
public class GameRecordDto {

	private String userId;
	private int games;
	private int wins;
	private int mmr;

========
public class ReadChatResponseDto {
	private int roomId;
	private int[] lastReadChatIdRange;
>>>>>>>> 3d0ae788e863679478fa948f0ba9242a66fda551:src/main/java/com/golab/talk/dto/ReadChatResponseDto.java
}
