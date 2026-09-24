package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.golab.talk.domain.Chatting;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Integer> {

	List<Chatting> findByIdLessThanAndRoomIdOrderByIdDesc(int cursor, int roomId, Pageable pageable);

	// notRead 관련 쿼리 제거 - Participant.lastReadChatId 로 읽음 상태 관리
	@Modifying
	@Transactional
	@Query(value = "insert into chatting(room_id, send_user_id, message) VALUES(?1, ?2, ?3)", nativeQuery = true)
	int insertByRoomId(int roomId, int sendUserId, String message);

	@Query(value = "select * from chatting where room_id=?1 order by id", nativeQuery = true)
	List<Chatting> getChattingListByRoomId(int roomId);

}
