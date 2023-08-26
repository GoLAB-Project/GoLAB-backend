package com.golab.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRecordDto {

	private String userId;
	private int games;
	private int wins;
	private int mmr;

}
