package com.golab.talk.service;

import com.golab.talk.domain.Participant;
import com.golab.talk.domain.Room;

public interface RoomService {

	Room getRoomByIdentifier(String identifier);

	Participant getRoomInfo(int userId, int roomId);

	Room createRoom(Room room);

}
