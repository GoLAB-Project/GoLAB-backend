package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
	private int id;
	private String name;
	private String statusMsg;
	private String profileImgUrl;
	private String backgroundImgUrl;

}
