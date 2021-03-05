package com.youseon.jpa.dto;

import java.util.List;

import com.youseon.jpa.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CategoryDto {
	
	private int categoryNo;
	private String categoryName;
	private List<Board> board;
	
	@Builder	
	public CategoryDto(int categoryNo, String categoryName, List<Board> board) {
		super();
		this.categoryNo = categoryNo;
		this.categoryName = categoryName;
		this.board = board;
	}
	
}
