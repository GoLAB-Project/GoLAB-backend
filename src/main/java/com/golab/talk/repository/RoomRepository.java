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

	Room findByIdentifier(String identifier);

	@Modifying
	@Transactional
	@Query(value = "insert into room(identifier, type, last_chat) values(?1, ?2, ?3)", nativeQuery = true)
	int createRoom(String identifier, String type, String lastChat);

	@Modifying
	@Transactional
	@Query(value = "update room set last_chat = ?1, updated_at = ?2 where identifier = ?3", nativeQuery = true)
	int updateByIdentifier(String lastChat, LocalDateTime updatedTime, String identifier);

	@Query(value = "select * from room where identifier like CONCAT(?1, '-%') or identifier like CONCAT('%-', ?1)", nativeQuery = true)
	List<Room> getRoomListByUserId(int userId);
}
