package com.youseon.jpa.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.youseon.jpa.dto.BoardDto;
import com.youseon.jpa.dto.CountBoardByAge;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.repository.BoardRepository;
import com.youseon.jpa.repository.BoardRepositoryImpl;
import com.youseon.jpa.repository.CustomBoardRepository;

@Service
public class BoardService {

	// autowired가 아닌 생성자 주입방식으로 인한 의존성 주입
	private final BoardRepository boardRepository;
	private final BoardRepositoryImpl boardRepositoryImpl;

	public BoardService(BoardRepository boardRepository, BoardRepositoryImpl boardRepositoryImpl) {
		this.boardRepositoryImpl = boardRepositoryImpl;
		this.boardRepository = boardRepository;
	}

	// 연령대별 회원 수
	@Transactional
	public List<Long> getAccountCount() {
		List<Long> queryResults = boardRepository.findAccountCounts();
		return queryResults;
	}

	// 연령대별 글 등록 수
	@Transactional
	public List<Long> getCount() {
		List<Long> queryResults = boardRepository.findCounts();
		return queryResults;
	}

	// 최근 글 등록 수
	@Transactional
	public List<Object> getWeekBoardCount() {
		List<Tuple> queryResults = boardRepository.findDateBoardCounts();
		List<Long> countList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		for (Tuple tuple : queryResults) {
			countList.add(tuple.get(0, Long.class));
			dateList.add(tuple.get(1, String.class));
		}
		
		List<Object> returnList = new ArrayList<>();
		returnList.add(countList);
		returnList.add(dateList);
		
		return returnList;
	}

	// 글 등록
	@Transactional
	public BoardDto saveBoard(BoardDto boardDto) {

		String imagePath = "C:\\Users\\yescnc\\youseon\\Project\\springVue\\jpa3\\src\\main\\resources\\static\\image\\";
		String boardFile = "";
		if(boardDto.getUploadFile() != null) {
			String fname = boardDto.getUploadFile().getOriginalFilename();
			boardFile = fname.substring(fname.lastIndexOf("\\")+1);

			if(boardFile != null && !boardFile.equals("")) {
				try {
					byte[] data = boardDto.getUploadFile().getBytes();
					FileOutputStream fos = new FileOutputStream(imagePath+"/"+boardFile);
					fos.write(data);
					fos.close();
				}catch (Exception e) {
					System.out.println("예외발생 : " + e.getMessage());
				}
			}
		}
		boardDto.setBoardFile(boardFile);
		Board board = boardRepository.save(boardDto.toEntity());
		return BoardDto.of(board);
	}

	// 모든 게시글
	// Page 객체를 리턴하며 Pageable과 마찬가지로 페이징 기능을 위해 추상화 시킨 인터페이스
	@Transactional(readOnly = true)
	public Page<BoardDto> getBoardList(String search, int categoryNo, Pageable pageable) {
		int pageNum = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
		int pageSize = 10;
		pageable = PageRequest.of(pageNum, pageSize, Sort.by("regdate").descending()); 

		Page<Board> p;
		if(search !=null && !search.equals("")) {
			p = boardRepository.findBoardByTitleContainingAndCategory_CategoryNo(search, categoryNo, pageable);
		} else {
			p = boardRepository.findBoardByCategory_CategoryNo(categoryNo,pageable);

		}
		return p.map(BoardDto::of);
	}

	// 선택 글 조회
	@Transactional
	public BoardDto getBoard(int boardNo) {
		Board board = boardRepository.findById(boardNo).get();
		board.updateHit(board.getHit()+1);
		return BoardDto.of(board);
	}

	// 선택 글 수정 dirtyChecking 사용
	@Transactional
	public BoardDto update(int boardNo, BoardDto boardDto) {
		Board board = boardRepository.findById(boardNo) 
				.orElseThrow(() -> new IllegalArgumentException(
						"해당 게시글이 없습니다. boardNo = " + boardNo));

		// 파일처리
		String imagePath = "C:\\Users\\yescnc\\youseon\\Project\\springVue\\jpa3\\src\\main\\resources\\static\\image\\";
		String oldFname = board.getBoardFile();
		String boardFile ="";

		// 새로 등록한 파일이 있을 경우
		if(boardDto.getUploadFile() != null) {
			String fname = boardDto.getUploadFile().getOriginalFilename();
			boardFile = fname.substring(fname.lastIndexOf("\\")+1);

			if(boardFile != null && !boardFile.equals("")) {
				try {
					byte[] data = (boardDto.getUploadFile()).getBytes();
					FileOutputStream fos = new FileOutputStream(imagePath+"/"+boardFile);
					fos.write(data);
					fos.close();
				}catch (Exception e) {
					System.out.println("예외발생s : " + e.getMessage());
				}
				boardDto.setBoardFile(boardFile);
			} else{
				boardDto.setBoardFile(oldFname);
			}

			// 새로 등록한 파일은 없는데 이전 파일이 존재 할 경우
		} else if(boardDto.getUploadFile()==null && oldFname!=null) {
			boardDto.setBoardFile(oldFname);

			// 새로운 파일이 등록됐고, 이전에 파일이 있었다면
		} else {
			if( oldFname != null && !oldFname.equals("") && !boardFile.equals("")) {
				File file = new File(imagePath + "/" +oldFname);
				file.delete();
			}
		}

		board.update(boardDto.getCategory(), boardDto.getAccount(), boardDto.getTitle(), boardDto.getContent(), boardDto.getBoardFile());

		return BoardDto.of(board);
	}

	// 선택 글 삭제
	@Transactional
	public void delete(int boardNo) {
		Board board = boardRepository.findById(boardNo) 
				.orElseThrow(() -> new IllegalArgumentException(
						"해당 게시글이 없습니다. boardNo = " + boardNo));

		String imagePath = "C:\\Users\\yescnc\\youseon\\Project\\springVue\\jpa3\\src\\main\\resources\\static\\image\\";

		if( board.getBoardFile() != null && !board.getBoardFile().equals("")) {
			File file = new File(imagePath + "/" +board.getBoardFile());
			file.delete();
		}
		boardRepository.deleteById(boardNo);
	}

	// 파일 선택
	@Transactional
	public File getImage(int boardNo) {
		Board board = boardRepository.findById(boardNo) 
				.orElseThrow(() -> new IllegalArgumentException(
						"해당 게시글이 없습니다. boardNo = " + boardNo));
		BoardDto boardDto = BoardDto.of(board);
		if(board.getBoardFile()!=null && !board.getBoardFile().equals("")) {
			File file = new File(
					"C:\\Users\\yescnc\\youseon\\Project\\springVue\\jpa3\\src\\main\\resources\\static\\image\\"+boardDto.getBoardFile());
			return file;
		} else {
			return null;
		}
	}

}
