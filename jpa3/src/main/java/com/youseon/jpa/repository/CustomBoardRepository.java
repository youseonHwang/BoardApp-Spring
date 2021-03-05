package com.youseon.jpa.repository;

import java.util.List;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.youseon.jpa.dto.CountBoardByAge;

public interface CustomBoardRepository {
	public List<Long> findCounts();
	public List<Long> findAccountCounts();
	public List<Tuple> findDateBoardCounts();
	
}
