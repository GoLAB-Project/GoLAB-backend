package com.golab.talk.service;

import java.util.List;

import com.golab.talk.dto.GameRoomDto;

// 비즈니스 로직 관련 코드

public interface GameRoomService {

	//게임방 생성
	GameRoomDto createGameRoom(GameRoomDto gameRoomDto);

	//게임방 list
	List<GameRoomDto> showGameRoomList();

	//게임방 list - roomtype
	List<GameRoomDto> showGameRoomListByRoomType(int gameRoomType);

	//게임방 이름으로 검색
	List<GameRoomDto> findByGameRoomNameContaining(String searchKeyword);

	//게임방 game_room_id로 조회
	GameRoomDto findByGameRoomId(int gameRoomId);

	//게임방 삭제 - 랭킹전
	int deleteGameRoom();

	//게임방 삭제 후 room_id 1로 초기화
	void resetGameRoomId();

	//Observer 키워드 랜덤 생성
	List<String> randomKeyword();

	//게임방 상태 변경
	void updateGameRoomStatus(int roomStatus, int gameRoomId);

}
