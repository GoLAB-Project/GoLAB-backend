package com.golab.talk.service;

import com.golab.talk.domain.Chatting;
import com.golab.talk.dto.ChattingPageResponse;

public interface ChattingService {

	// Cursor Pagination - 최근 N개 조회 (cursor = Integer.MAX_VALUE 로 호출 시 최신)
	ChattingPageResponse getChattingPage(int roomId, int cursor, int size);

	Chatting saveChat(int roomId, int sendUserId, String message);

}
