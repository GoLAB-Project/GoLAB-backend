package com.golab.talk.dto;

import java.util.List;

import com.golab.talk.domain.Chatting;
import com.golab.talk.domain.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocketSendDto {
	private List<Room> roomList;
	private List<Chatting> chattingList;
}
