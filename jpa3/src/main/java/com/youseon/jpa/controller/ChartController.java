package com.youseon.jpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.youseon.jpa.service.BoardService;
import com.youseon.jpa.service.ReviewService;

import io.swagger.annotations.ApiOperation;

@Controller
public class ChartController {
	private BoardService boardService;
	private ReviewService reviewService;

    public ChartController(BoardService boardService, ReviewService reviewService) {
        this.boardService = boardService;
        this.reviewService = reviewService;
    }
    
    @CrossOrigin
    @GetMapping("api/count/account")
    @ApiOperation(value = "연령대별 회원수 차트")
    public  @ResponseBody List<Long> getAcountCount() {
    	System.out.println(boardService.getAccountCount());
    	List<Long> forChart = boardService.getAccountCount();
    	return forChart;
    }
    
    @CrossOrigin
    @GetMapping("api/count/age")
    @ApiOperation(value = "연령대별 게시물 글 등록수 차트")
    public  @ResponseBody List<Long> getCount() {
    	System.out.println(boardService.getCount());
    	List<Long> forChart = boardService.getCount();
    	return forChart;
    }
    
    @CrossOrigin
    @GetMapping("api/count/week")
    @ApiOperation(value = "최근 게시물 글 등록수 차트")
    public @ResponseBody List<Object> getWeekBoardCount() {
    	System.out.println("getWeekBoardCount입장");
    	System.out.println(boardService.getWeekBoardCount());
    	List<Object> forChart = boardService.getWeekBoardCount();
    	return forChart;
    }
    
    @CrossOrigin
    @GetMapping("api/count/rating")
    @ApiOperation(value = "최근 게시물 글 등록수 차트")
    public @ResponseBody List<Long> getRatingCount() {
    	return reviewService.getRatingCount();
    }
}
