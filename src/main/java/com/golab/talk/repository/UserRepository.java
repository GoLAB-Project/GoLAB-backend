package com.golab.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT nickname FROM user WHERE nickname = :nickname", nativeQuery = true)
	String checkNickname(String nickname);

	@Query(value = "SELECT user_id FROM user WHERE user_id = :userId", nativeQuery = true)
	String checkUserId(String userId);

	@Query(value = "SELECT email FROM user WHERE email = :email", nativeQuery = true)
	String checkEmail(String email);

	@Query(value = "SELECT user_id FROM user WHERE email = :email", nativeQuery = true)
	String findUserIdByEmail(String email);

	@Query(value = "SELECT password FROM user WHERE email = :email AND user_id = :userId", nativeQuery = true)
	String findPasswordByEmailAndId(String email, String userId);

	User findUserByUserIdAndPassword(String userId, String password);
}
