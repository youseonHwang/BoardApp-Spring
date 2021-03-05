package com.youseon.jpa.service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.youseon.jpa.dto.BoardDto;
import com.youseon.jpa.dto.ReviewDto;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.entity.Review;
import com.youseon.jpa.repository.BoardRepository;
import com.youseon.jpa.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public ReviewService (ReviewRepository reviewRepository) {
		this.reviewRepository =reviewRepository;
	}

	// 리뷰 등록
	@Transactional
	public ReviewDto saveBoard(ReviewDto reviewDto) {
		Review review = reviewRepository.save(reviewDto.toEntity());
		return ReviewDto.of(review);
	}
	
	// 각 점수대 별 인원 수 차트
	@Transactional
	public List<Long> getRatingCount() {
		return reviewRepository.findReviewCounts();
	}

	// 리뷰 등록
	@Transactional
	public List<ReviewDto> getReviews() {
		List<Review> list = reviewRepository.findAll();
		
		return list.stream() // 스트림 생성 ( List와 같은 컬렉션 인스턴스를 스트림으로 생성합니다.)
				.map(review -> ReviewDto.of(review)) // 가공 
				.collect(Collectors.toList()); // 결과 반환 (가공된 Stream을 List라는 최종 결과로 만들어 반환)
	}
}
