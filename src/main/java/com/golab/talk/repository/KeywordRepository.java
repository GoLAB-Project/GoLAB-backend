package com.golab.talk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.golab.talk.domain.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	//모든 키워드 조회
	@Query(value = "SELECT * FROM keyword", nativeQuery = true)
	List<Keyword> findAll();

}
