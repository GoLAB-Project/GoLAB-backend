package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.Participant;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

	@Query(
		value =
			"SELECT * FROM participant p JOIN room r ON p.room_id = r.id " +
				"WHERE p.user_id = :userId AND r.last_chat <> ''", nativeQuery = true)
	List<Participant> findRoomDataByUserId(int userId);

	@Query(value = "SELECT user_id FROM participant WHERE room_id = :roomId", nativeQuery = true)
	List<Integer> getParticipantIdList(int roomId);

}
