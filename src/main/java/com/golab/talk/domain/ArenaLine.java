package com.golab.talk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArenaLine {

	public static final String PLAYER = "PLAYER";
	public static final String AI = "AI";
	public static final String JUDGE = "JUDGE";

	private final String speaker;
	private final ArenaPhase phase;
	private final String text;
}
