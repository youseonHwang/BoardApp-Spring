package com.youseon.jpa.repository;

import java.util.List;

import com.querydsl.core.Tuple;

public interface CustomReviewRepository {
	
	public List<Long> findReviewCounts();
}
