package com.golab.talk.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChattingResponseDto {

	private int id;
	private int roomId;
	private int sendUserId;
	private String message;
	private int notRead;
	private LocalDateTime createdAt;

}
