package com.youseon.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.youseon.jpa.entity.Account;

/**
 * 
 * Repository는 데이터 조작을 담당하며, JpaRepository를 상속
 * JpaRepository의 값은 매핑할 Entity와 Id의 타입
 *
 */
public interface AccountRepository extends JpaRepository<Account, String> {
	Account findAccountById(String id);
	
}