package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.Ban;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {

	void deleteByMyIdAndBanId(int myId, int banId);

	@Query(value = "SELECT ban_id FROM ban WHERE my_id = :myId", nativeQuery = true)
	List<String> findByMyId(int myId);

}
