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

	// Repository + Service + Controller 로직 정비

	// 채팅 생성 -> {roomId, sendUserId, message, 1, datetime, datetime}
	// 채팅 정보 업데이트 -> (updateTime? 관련 로직 + sendUserId가 내 id와 다를 때) notRead가 1 이상이라면 notRead -= 1 {notRead : 0, updatedAt : datetime}
	// 채팅방 정보 업데이트 -> identifier("1-2")를 통해 id를 추출하고, 두 아이디의 채팅방에 대해 {lastChat : message, updatedAt : datetime}

	// (발신)채팅을 보냈을 때
	// 채팅 생성
	// 채팅방 정보 업데이트

	// (수신)채팅을 읽었을 때
	// 채팅 정보 업데이트
	// 채팅방 정보 업데이트

	/* 2023-10-05 20:00 추가
	 * 우선 not Read는 구현하지 않는다.
	 */

	@Modifying
	@Transactional
	@Query(value = "insert into chatting(room_id, send_user_id, message, not_read) VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	int insertByRoomId(int roomId, int sendUserId, String message, int notRead);

	@Query(value = "select * from chatting where room_id=?1 order by id", nativeQuery = true)
	List<Chatting> getChattingListByRoomId(int roodId);
}
