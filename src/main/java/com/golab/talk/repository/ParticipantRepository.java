package com.golab.talk.repository;

import java.util.List;

import com.golab.talk.dto.ChattingListDto;
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

	@Query(value = "SELECT * FROM participant WHERE user_id = :userId AND room_id = :roomId", nativeQuery = true)
	Participant findByUserIdAndRoomId(int userId, int roomId);


	//범석 추가
	@Query(value = "SELECT u.profile_img_url as profile_img_url, u.name as name, p.room_id as room_id, u.id as id " +
			"FROM user u INNER JOIN participant p ON u.id = p.user_id " +
			"WHERE p.room_id IN (SELECT room_id FROM participant WHERE user_id = :userId) AND p.user_id <> :userId", nativeQuery = true)
	List<ChattingListDto> getRoomByUserId(int userId);

}
