package com.youseon.jpa.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Category {
	
	@Id // 카테고리 PK
	private int categoryNo;
	
	@Column(length = 50, nullable = false) // 카테고리명
	private String categoryName;
	
//  추후 차트 기능을 위해 연관관계 남겨둠
//	@OneToMany(mappedBy = "category")
//	@JsonBackReference
//	private List<Board> board;
		
	@Builder	
	public Category(int categoryNo, String categoryName) {
		this.categoryNo = categoryNo;
		this.categoryName = categoryName;
	}
	
}
