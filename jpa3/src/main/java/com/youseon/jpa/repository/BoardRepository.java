package com.youseon.jpa.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.youseon.jpa.dto.CountBoardByAge;
import com.youseon.jpa.entity.Board;

/**
 * Repository는 데이터 조작을 담당하며, JpaRepository를 상속
 * JpaRepository의 값은 매핑할 Entity와 Id의 타입
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>, CustomBoardRepository {
	
	
	public Page<Board> findBoardByCategory_CategoryNo(int categoryNo, Pageable pageable);
	public Page<Board> findBoardByTitleContainingAndCategory_CategoryNo(@Param("search")String search, int categoryNo, Pageable pageable);
	
}