package com.golab.talk.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

	Room findById(int roomId);

	// identifier 관련 메서드 제거 - roomId 중심으로 통일
	@Modifying
	@Transactional
	@Query(value = "insert into room(type, last_chat) values(?1, ?2)", nativeQuery = true)
	int createRoom(String type, String lastChat);

	@Modifying
	@Transactional
	@Query(value = "update room set last_chat = ?1, updated_at = ?2 where id = ?3", nativeQuery = true)
	int updateById(String lastChat, LocalDateTime updatedTime, int roomId);

}
