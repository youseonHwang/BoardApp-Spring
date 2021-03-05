package com.youseon.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate // Null 필드를 update쿼리에서 제외하고자 하면 (값이 변경된 필드만 업데이트)
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) 
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다.(JPA Auditing을 사용하면 생성시간과 수정시간을 자동으로 관리)
@Table(name="Board")
public class Board implements Serializable{

	@ManyToOne(optional = false, cascade = CascadeType.PERSIST) // FK 카테고리 컬럼명으로Category에 대한 외래키 설정이 됩니다.
	@JoinColumn(name="categoryNo",nullable = false)
	private Category category; 
	
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST) // FK 유저
	@JoinColumn(name="ID", nullable = false)
	@JsonManagedReference
	private Account account;

	@Id // PK 게시글 번호
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int boardNo;

	@Column(length = 50, nullable = false) // 글 제목
	private String title;
	
	@Column(length = 1000, nullable = false) // 글 내용
	private String content;

	private String boardFile; // 파일 이름

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 등록일
	@CreationTimestamp // 데이터를 Insert해줄때 자동으로 시간을 입력
	private LocalDateTime regdate;

	@Column(nullable = false) // 조회수 
	private int hit;
	
	@Builder //해당 클래스의 빌더 패턴 클래스를 생성
	public Board(Category category, int boardNo, Account account, String title, String content, String boardFile,
			LocalDateTime regdate, int hit) {
		this.category = category;
		this.boardNo = boardNo;
		this.account = account;
		this.title = title;
		this.content = content;
		this.boardFile = boardFile;
		this.regdate = regdate;
		this.hit = hit;
	}
	
	// update할 해당 정보를 받고 게시글의 상태를 바꿔주는 메소드
	public void update(Category category, Account account, String title, String content, String boardFile) {
		this.category=category;
		this.account=account;
		this.title=title;
		this.content=content;
		this.boardFile=boardFile;
	}
	
	public void updateHit(int hit) {
		this.hit=hit;
	}

	
}
