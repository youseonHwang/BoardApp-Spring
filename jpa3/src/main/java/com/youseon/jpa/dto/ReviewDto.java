package com.youseon.jpa.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.youseon.jpa.entity.Account;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.entity.Category;
import com.youseon.jpa.entity.Review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewDto {
	private Account account;
	private int reviewNo;
	private String reviewMemo;
	private LocalDateTime regdate;
	private Long rating;
	
	public Review toEntity() {
		Review review = Review.builder()
				.reviewNo(reviewNo)
				.reviewMemo(reviewMemo)
				.regdate(regdate)
				.account(account)
				.rating((Long)rating)
				.build();
		return review;
	}
	
	@Builder
	public ReviewDto(Account account, int reviewNo, String reviewMemo, LocalDateTime regdate, Long rating) {
		super();
		this.account = account;
		this.reviewNo = reviewNo;
		this.reviewMemo = reviewMemo;
		this.regdate = regdate;
		this.rating = (Long)rating;
	}
	
	public static ReviewDto of(Review review){
        return ReviewDto.builder()
        		.reviewNo(review.getReviewNo())
                .reviewMemo(review.getReviewMemo())
                .account(review.getAccount())
                .regdate(review.getRegdate())
                .rating((Long)review.getRating())
                .build();
    }
}
