package com.golab.talk.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArenaMatch {

	private String id;
	private String topic;
	private String playerStance;
	private String aiStance;
	private String constraint;
	private String requiredWord;
	private String forbiddenWord;
	private String persona;
	private ArenaPhase phase = ArenaPhase.OPENING;
	private final List<ArenaLine> lines = new ArrayList<>();
	private int playerScore;
	private int aiScore;
	private boolean playerUsedRequiredWord;
	private boolean finished;
	private String winner;
	private String verdict;
}
