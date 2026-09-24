package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

	@Query(value = "SELECT * FROM participant WHERE room_id = :roomId", nativeQuery = true)
	List<Participant> findByRoomId(int roomId);

	// 두 사용자가 함께 참여한 1:1 Room 조회
	@Query(value =
		"SELECT p1.room_id FROM participant p1 " +
		"JOIN participant p2 ON p1.room_id = p2.room_id " +
		"JOIN room r ON p1.room_id = r.id " +
		"WHERE p1.user_id = :userAId AND p2.user_id = :userBId AND r.type = 'ONE_TO_ONE' " +
		"LIMIT 1", nativeQuery = true)
	Integer findOneToOneRoomId(int userAId, int userBId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE participant SET last_read_chat_id = :lastReadChatId, updated_at = NOW() " +
		"WHERE user_id = :userId AND room_id = :roomId", nativeQuery = true)
	int updateLastReadChatId(int userId, int roomId, int lastReadChatId);

}
