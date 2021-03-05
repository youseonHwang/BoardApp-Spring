package com.youseon.jpa.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youseon.jpa.dto.BoardDto;
import com.youseon.jpa.dto.ReviewDto;
import com.youseon.jpa.repository.BasicResponse;
import com.youseon.jpa.repository.CommonResponse;
import com.youseon.jpa.repository.ErrorResponse;
import com.youseon.jpa.service.BoardService;
import com.youseon.jpa.service.ReviewService;

import io.swagger.annotations.ApiOperation;

@Controller
public class ReviewController {
	
	private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
    
    // 새 리뷰 등록
    @CrossOrigin
    @PostMapping("api/review")
    @ApiOperation(value = "새 리뷰 등록")
    public @ResponseBody BasicResponse write(ReviewDto reviewDto) {
    	System.out.println(reviewDto.toString());
        if(reviewDto == null) {
        	return new ErrorResponse("글 등록 실패");
        }
        return new CommonResponse<ReviewDto>(reviewService.saveBoard(reviewDto));
    }
    
    // 모든 리뷰목록 반환 (리뷰 목록)
    @CrossOrigin
    @GetMapping("api/review")
    @ApiOperation(value = "리뷰글 조회")
    public @ResponseBody List<ReviewDto> getReviews() {
    	System.out.println("getReviews 들어옴");
    		List<ReviewDto> list = reviewService.getReviews();
        return list;
    }
    
    
}
