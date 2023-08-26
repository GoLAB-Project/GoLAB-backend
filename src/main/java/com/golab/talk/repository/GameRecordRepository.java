package com.golab.talk.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.GameRecord;

@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, String> {

	//내 전적 조회
	@Query(value = "SELECT A.user_id AS userId, A.games, A.wins, A.mmr, RANK() OVER(ORDER BY MMR DESC, WINS DESC) ranking FROM game_record A WHERE user_id = ?1", nativeQuery = true)
	Map<String, Object> findByUserIdWithRanking(String userId);

	//나와 상대의 전적 조회
	@Query(value = "SELECT A.*, RANK() OVER(ORDER BY MMR DESC, WINS DESC) ranking FROM game_record A WHERE user_id = ?1 OR user_id = ?2", nativeQuery = true)
	List<GameRecord> findMMRWithRanking(String userId1, String userId2);

	//전체 랭킹 조회
	@Query(value = "SELECT A.user_id AS userId, A.games, A.wins, A.mmr, RANK() OVER(ORDER BY MMR DESC, WINS DESC) ranking FROM game_record A", nativeQuery = true)
	List<Map<String, Object>> findAllByOrderByMmrAscWithRanking();

}
