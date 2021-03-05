package com.youseon.jpa.entity;

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
@EntityListeners(AuditingEntityListener.class) // JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다.(JPA Auditing을 사용하면 생성시간과 수정시간을 자동으로 관리)
@Table(name="Review")
public class Review{
	
	@ManyToOne(optional = false, cascade = CascadeType.PERSIST) // FK 유저
	@JoinColumn(name="ID", nullable = false)
	@JsonManagedReference
	private Account account;

	@Id // PK 게시글 번호
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reviewNo;
	
	@Column(length = 1000) // 글 내용
	private String reviewMemo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 등록일
	@CreationTimestamp // 데이터를 Insert해줄때 자동으로 시간을 입력
	private LocalDateTime regdate;
	
	@Column(nullable = false)
	private Long rating;
	
	@Builder //해당 클래스의 빌더 패턴 클래스를 생성
	public Review(Account account, int reviewNo, String reviewMemo, LocalDateTime regdate, Long rating) {
		super();
		this.account = account;
		this.reviewNo = reviewNo;
		this.reviewMemo = reviewMemo;
		this.regdate = regdate;
		this.rating = rating;
	}
	
}
