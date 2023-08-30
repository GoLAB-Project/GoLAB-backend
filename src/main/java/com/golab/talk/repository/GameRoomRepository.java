package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.GameRoom;

// db 접근하는 코드들
@Repository
public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {

	//게임방 생성
	@Query(value =
		"INSERT INTO game_room (game_room_id, game_room_type, max_participants, game_room_name, game_room_pw, room_status) "
			+ "VALUES (:gameRoomId, :gameRoomType, :maxParticipants, :gameRoomName, :gameRoomPW, :roomStatus)", nativeQuery = true)
	GameRoom createGameRoom(int gameRoomId, int gameRoomType, int maxParticipants, String gameRoomName,
		String gameRoomPW, int roomStatus);

	// 게임방 리스트
	@Query(value = "SELECT * FROM game_room", nativeQuery = true)
	List<GameRoom> showGameRoomList();

	// 게임방 리스트 - roomtype
	@Query(value = "SELECT * FROM game_room WHERE game_room_type = :gameRoomType", nativeQuery = true)
	List<GameRoom> showGameRoomListByRoomType(int gameRoomType);

	//게임방 이름으로 검색
	@Query(value = "SELECT * FROM game_room WHERE game_room_name LIKE %:searchKeyword%", nativeQuery = true)
	List<GameRoom> findByGameRoomNameContaining(String searchKeyword);

	//게임방 game_room_id로 검색
	@Query(value = "SELECT * FROM game_room WHERE game_room_id = :gameRoomId", nativeQuery = true)
	GameRoom findByGameRoomId(int gameRoomId);

	//게임방 삭제
	@Modifying
	@Query(value = "DELETE FROM game_room A WHERE A.game_room_id in :gameRoomId", nativeQuery = true)
	void deleteGameRoom(@Param("gameRoomId") List<Integer> gameRoomId);

	//게임방 삭제 후 room_id 1로 초기화
	@Modifying
	@Query(value = "ALTER TABLE game_room AUTO_INCREMENT = 1", nativeQuery = true)
	void resetGameRoomId();

	//게임 시작 후
	//키워드 랜덤 3개 생성
	@Query(value = "SELECT * FROM keyword ORDER BY RAND() LIMIT 3", nativeQuery = true)
	List<String> randomKeyword();

	//게임방 상태 변경
	@Query(value = "UPDATE game_room SET room_status = :roomStatus WHERE game_room_id = :gameRoomId", nativeQuery = true)
	void updateGameRoomStatus(int roomStatus, int gameRoomId);

}
