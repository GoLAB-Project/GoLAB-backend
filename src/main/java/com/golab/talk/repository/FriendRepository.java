package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

	void deleteByMyIdAndFriendId(int myId, int friendId);

	@Query(value = "SELECT friend_id FROM friend WHERE my_id = :myId", nativeQuery = true)
	List<String> findByMyId(int myId);

}
