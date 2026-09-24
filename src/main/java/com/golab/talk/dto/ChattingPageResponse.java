package com.golab.talk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChattingPageResponse {
	private List<ChattingResponseDto> chattingList;
	private Integer nextCursor; // 다음 조회 시 사용할 cursor (가장 오래된 chatting id)
	private boolean hasNext;
}
