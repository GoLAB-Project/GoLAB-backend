package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadChatRequestDto {
	private String userId;
	private int roomId;
	private String type;
	private UserResponseDto[] participant;
	private int[] lastReadChatIdRange;
}
