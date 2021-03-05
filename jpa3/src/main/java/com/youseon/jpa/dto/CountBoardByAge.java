package com.youseon.jpa.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class CountBoardByAge {
	
	private String ageGroup;
	private int count;
	
	@QueryProjection
    public CountBoardByAge(String ageGroup, int count) {
        this.ageGroup = ageGroup;
        this.count = count;
    }
}
