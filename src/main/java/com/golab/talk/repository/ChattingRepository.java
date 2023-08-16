package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.Chatting;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Integer> {

	List<Chatting> findByIdLessThanAndRoomIdOrderByIdDesc(int cursor, int roomId, Pageable pageable);

}
