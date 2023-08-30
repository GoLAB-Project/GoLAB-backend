package com.golab.talk.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.golab.talk.domain.GameRoom;
import com.golab.talk.dto.GameRoomDto;
import com.golab.talk.repository.GameRoomRepository;
import com.golab.talk.service.GameRoomService;

@Service
public class GameRoomServiceImpl implements GameRoomService {

	@Autowired
	private GameRoomRepository gameRoomRepository;

	@Override
	public GameRoomDto createGameRoom(GameRoomDto gameRoomDto) {
		GameRoom gameRoom = new GameRoom(
			gameRoomDto.getGameRoomId(),
			gameRoomDto.getGameRoomType(),
			gameRoomDto.getMaxParticipants(),
			gameRoomDto.getGameRoomName(),
			gameRoomDto.getGameRoomPW(),
			gameRoomDto.getRoomStatus()
		);

		gameRoomRepository.save(gameRoom);

		return gameRoomDto;
	}

	@Override
	public List<GameRoomDto> showGameRoomList() {
		List<GameRoom> list = gameRoomRepository.showGameRoomList();

		return list.stream().map(gameRoom -> new GameRoomDto(
				gameRoom.getGameRoomId(),
				gameRoom.getGameRoomType(),
				gameRoom.getMaxParticipants(),
				gameRoom.getGameRoomName(),
				gameRoom.getGameRoomPW(),
				gameRoom.getRoomStatus()
			))
			.collect(java.util.stream.Collectors.toList());
	}

	@Override
	public List<GameRoomDto> showGameRoomListByRoomType(int gameRoomType) {
		List<GameRoom> list = gameRoomRepository.showGameRoomListByRoomType(gameRoomType);

		return list.stream().map(gameRoom -> new GameRoomDto(
				gameRoom.getGameRoomId(),
				gameRoom.getGameRoomType(),
				gameRoom.getMaxParticipants(),
				gameRoom.getGameRoomName(),
				gameRoom.getGameRoomPW(),
				gameRoom.getRoomStatus()
			))
			.collect(java.util.stream.Collectors.toList());
	}

	@Override
	public List<GameRoomDto> findByGameRoomNameContaining(String searchKeyword) {
		List<GameRoom> list = gameRoomRepository.findByGameRoomNameContaining(searchKeyword);

		return list.stream().map(gameRoom -> new GameRoomDto(
				gameRoom.getGameRoomId(),
				gameRoom.getGameRoomType(),
				gameRoom.getMaxParticipants(),
				gameRoom.getGameRoomName(),
				gameRoom.getGameRoomPW(),
				gameRoom.getRoomStatus()
			))
			.collect(java.util.stream.Collectors.toList());
	}

	@Override
	public GameRoomDto findByGameRoomId(int gameRoomId) {
		GameRoom gameRoom = gameRoomRepository.findByGameRoomId(gameRoomId);

		return new GameRoomDto(
			gameRoom.getGameRoomId(),
			gameRoom.getGameRoomType(),
			gameRoom.getMaxParticipants(),
			gameRoom.getGameRoomName(),
			gameRoom.getGameRoomPW(),
			gameRoom.getRoomStatus()
		);
	}

	@Override
	public int deleteGameRoom() {
		List<Integer> gameRoomId = new ArrayList<>();
		List<GameRoom> list = gameRoomRepository.showGameRoomList();
		for (GameRoom gameroom : list) {
			gameRoomId.add(gameroom.getGameRoomId());
		}
		gameRoomRepository.deleteGameRoom(gameRoomId);

		if (gameRoomRepository.showGameRoomList().isEmpty()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public void resetGameRoomId() {
		gameRoomRepository.resetGameRoomId();
	}

	@Override
	public List<String> randomKeyword() {
		List<String> list;
		list = gameRoomRepository.randomKeyword();
		return list;
	}

	@Override
	public void updateGameRoomStatus(int roomStatus, int gameRoomId) {
		gameRoomRepository.updateGameRoomStatus(roomStatus, gameRoomId);
	}
}
