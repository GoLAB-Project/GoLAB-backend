package com.golab.talk.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArenaMatchResponse {

	private String matchId;
	private String topic;
	private String playerStance;
	private String aiStance;
	private String constraint;
	private String persona;
	private String phase;
	private String phaseHint;
	private int phaseSeconds;
	private boolean playerTurn;
	private boolean finished;
	private String winner;
	private String verdict;
	private int playerScore;
	private int aiScore;
	private List<ArenaLineDto> lines;
}
