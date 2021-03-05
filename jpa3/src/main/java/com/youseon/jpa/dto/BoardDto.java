package com.youseon.jpa.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.youseon.jpa.entity.Account;
import com.youseon.jpa.entity.Board;
import com.youseon.jpa.entity.Category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * vo와 비슷 Controller와 Service 사이에서 데이터를 주고받는 DTO(Data Access Object)
 * dto 패키지를 만들고, 그 안에 BoardDto 클래스
 * toEntity()는 DTO에서 필요한 부분을 빌더 패턴을 통해 Entity로 만드는 일
 * @author 유선
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto implements Serializable{
	private Category category;
	private int boardNo;
	private Account account;
	private String title;
	private String content;
	private String boardFile;
	private LocalDateTime regdate;
	private int hit;
	private MultipartFile uploadFile;

	public Board toEntity() {
		Board build = Board.builder()
				.category(category)
				.boardNo(boardNo)
				.account(account)
				.title(title)
				.content(content)
				.boardFile(boardFile)
				.regdate(regdate)
				.hit(hit)
				.build();
		return build;
	}
	
	@Builder
	public BoardDto(Category category, int boardNo, Account account, String title, String content, String boardFile,
			LocalDateTime regdate, int hit, MultipartFile uploadFile) {
		super();
		this.category = category;
		this.boardNo = boardNo;
		this.account = account;
		this.title = title;
		this.content = content;
		this.boardFile = boardFile;
		this.regdate = regdate;
		this.hit = hit;
		this.uploadFile = uploadFile;
	}

	@Builder
	public BoardDto(Category category, int boardNo, Account account, String title, String content, String boardFile,
			LocalDateTime regdate, int hit) {
		super();
		this.category = category;
		this.boardNo = boardNo;
		this.account = account;
		this.title = title;
		this.content = content;
		this.boardFile = boardFile;
		this.regdate = regdate;
		this.hit = hit;
	}
	
	public static BoardDto of(Board board){
        return BoardDto.builder()
        		.category(board.getCategory())
                .boardNo(board.getBoardNo())
                .account(board.getAccount())
                .title(board.getTitle())
                .content(board.getContent())
                .boardFile(board.getBoardFile())
                .regdate(board.getRegdate())
                .hit(board.getHit())
                .build();
    }
	@Builder
	public void update(Category category, Account account, String title, String content, String boardFile) {
		this.category=category;
		this.account=account;
		this.title=title;
		this.content=content;
		this.boardFile=boardFile;
	}
	
	public BoardDto (Board board) {
	}
}
