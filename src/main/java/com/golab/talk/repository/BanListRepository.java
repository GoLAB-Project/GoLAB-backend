package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.BanList;

@Repository
public interface BanListRepository extends JpaRepository<BanList, Long> {

	void deleteByUserIdAndBanId(String userId, String banId);

	@Query(value = "SELECT ban_id FROM ban_list WHERE user_id = :userId", nativeQuery = true)
	List<String> findByUserId(String userId);

}
