package com.golab.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.golab.talk.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

	Room findById(int roomId);

}
