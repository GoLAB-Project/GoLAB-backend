package com.golab.talk.domain;

public enum ArenaPhase {
	OPENING(45, "오프닝 · 주장 1개와 이유 1개"),
	CROSS(30, "크로스 · 상대 마지막 문장을 받고 반박"),
	CLOSING(20, "클로징 · 한 문장 훅. 새 근거 금지"),
	VERDICT(0, "판정");

	private final int seconds;
	private final String hint;

	ArenaPhase(int seconds, String hint) {
		this.seconds = seconds;
		this.hint = hint;
	}

	public int getSeconds() {
		return seconds;
	}

	public String getHint() {
		return hint;
	}

	public ArenaPhase next() {
		if (this == OPENING) {
			return CROSS;
		}
		if (this == CROSS) {
			return CLOSING;
		}
		return VERDICT;
	}
}
