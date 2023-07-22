package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.FriendList;

@Repository
public interface FriendListRepository extends JpaRepository<FriendList, Long> {

	void deleteByUserIdAndFriendId(String userId, String friendId);

	@Query(value = "SELECT friend_id FROM friend_list WHERE user_id = :userId", nativeQuery = true)
	List<String> findByUserId(String userId);

}
