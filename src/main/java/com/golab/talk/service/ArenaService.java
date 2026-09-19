package com.golab.talk.service;

import com.golab.talk.dto.ArenaMatchResponse;

public interface ArenaService {

	ArenaMatchResponse start();

	ArenaMatchResponse get(String matchId);

	ArenaMatchResponse speak(String matchId, String text);
}
