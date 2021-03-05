package com.youseon.jpa.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.youseon.jpa.dto.BoardDto;
import com.youseon.jpa.repository.BasicResponse;
import com.youseon.jpa.repository.CommonResponse;
import com.youseon.jpa.repository.ErrorResponse;
import com.youseon.jpa.service.BoardService;

import io.swagger.annotations.ApiOperation;


@Controller
public class BoardController {
	
	private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    
    // 모든 게시물을 반환 (게시판 목록)
    @CrossOrigin
    @GetMapping("api/board")
    @ApiOperation(value = "요청 카테고리별 게시판리스트 조회")
    public @ResponseBody Page<BoardDto> boardView(
    		@RequestParam(value="categoryNo", required = false, defaultValue = "1000")int categoryNo,
    		@RequestParam(value="search", required = false, defaultValue = "")String search,
    		 final Pageable pageable) {
    		System.out.println(pageable);
    		System.out.println(search);
    		Page<BoardDto> page = boardService.getBoardList(search, categoryNo, pageable);
        return page;
    }

    // 새 글 등록
    @CrossOrigin
    @PostMapping("api/board")
    @ApiOperation(value = "새 글 등록")
    public @ResponseBody BasicResponse write(BoardDto boardDto) {
        if(boardDto == null) {
        	return new ErrorResponse("글 등록 실패");
        }
        return new CommonResponse<BoardDto>(boardService.saveBoard(boardDto));
    }
    
    // 선택 글 조회
    @CrossOrigin
    @GetMapping("api/board/{boardNo}")
    @ApiOperation(value = "선택한 글 상세 조회")
    public @ResponseBody BoardDto detail(@PathVariable("boardNo") int boardNo) {
        BoardDto boardDto = boardService.getBoard(boardNo);
        return boardDto;
    }
    
    // 선택 글 수정
    @CrossOrigin
    @PutMapping("api/board/{boardNo}")
    @ApiOperation(value = "선택한 글 수정")
    public @ResponseBody BasicResponse update(@PathVariable("boardNo")int boardNo, BoardDto boardDto) {
    	if(boardDto == null) {
        	return new ErrorResponse("글 수정 실패");
        }
        return new CommonResponse<BoardDto>(boardService.update(boardNo, boardDto));
    } 
    
    // 선택 글 삭제
    @CrossOrigin
    @DeleteMapping("api/board/{boardNo}")
    @ApiOperation(value = "선택한 글 삭제")
    public @ResponseBody ResponseEntity<Void> delete(@PathVariable("boardNo")int boardNo) {
    	boardService.delete(boardNo);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    
    // 파일 가져오기
    @CrossOrigin
    @GetMapping(value= "api/image/{boardNo}" )
    @ApiOperation(value = "선택한 게시물의 파일 가져오기")
    public @ResponseBody void getImage(@PathVariable("boardNo")int boardNo, HttpServletResponse res) {
    	
    	if(boardService.getImage(boardNo)!=null && !boardService.getImage(boardNo).getName().equals("")) {
    		File file = boardService.getImage(boardNo);
        	String extension =  FilenameUtils.getExtension(file.getName());
        	
        	
        	res.setContentLength((int) file.length());
        	
    		if(extension!= null && extension.equals("pdf")) { // pdf일 경우
    			res.setContentType("application/pdf");
    		}else {
    			res.setContentType(MediaType.IMAGE_JPEG_VALUE);
    		}
    		
    		try {
    			FileCopyUtils.copy(new FileInputStream(file), res.getOutputStream());
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} 
	}
}
