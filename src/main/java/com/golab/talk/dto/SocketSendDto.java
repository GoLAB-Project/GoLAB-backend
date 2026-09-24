package com.golab.talk.dto;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SocketSendDto {
	private String type;    // CHAT | READ | ROOM_UPDATE
	private int roomId;
	private Integer userId;
	private Integer lastReadChatId;
	private Chatting chatting;  // CHAT 이벤트 시
	private Room room;          // CHAT / ROOM_UPDATE 이벤트 시
}
