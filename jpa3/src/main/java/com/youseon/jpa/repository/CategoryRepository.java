package com.youseon.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.youseon.jpa.entity.Category;

/**
 * 
 * Repository는 데이터 조작을 담당하며, JpaRepository를 상속
 * JpaRepository의 값은 매핑할 Entity와 Id의 타입
 *
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
}