package com.golab.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT name FROM user WHERE name = :name", nativeQuery = true)
    String checkName(String name);

    @Query(value = "SELECT user_id FROM user WHERE user_id = :userId", nativeQuery = true)
    String checkUserId(String userId);

    @Query(value = "SELECT email FROM user WHERE email = :email", nativeQuery = true)
    String checkEmail(String email);

    @Query(value = "SELECT user_id FROM user WHERE email = :email", nativeQuery = true)
    String findUserIdByEmail(String email);

    @Query(value = "SELECT password FROM user WHERE email = :email AND user_id = :userId", nativeQuery = true)
    String findPasswordByEmailAndId(String email, String userId);

    User findUserByUserIdAndPassword(String userId, String password);

    @Query(value = "SELECT password FROM user WHERE user_id = :userId", nativeQuery = true)
    String checkPassword(String userId);

    @Modifying
    @Query(value = "UPDATE user SET password = :password WHERE user_id = :userId", nativeQuery = true)
    int updatePassword(String userId, String password);

    @Modifying
    @Query(value = "UPDATE user SET name = :name WHERE user_id = :userId", nativeQuery = true)
    int updateName(String userId, String name);

}