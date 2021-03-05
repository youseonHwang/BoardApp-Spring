package com.youseon.jpa.repository;

import lombok.Getter;
import lombok.Setter;

/**
 * api 요청 실패시엔 errorMessage와 errorCode를 반환
 * @author youseon
 *
 */
@Getter @Setter
public class ErrorResponse extends BasicResponse {
	
	private String errorMessage;
	private String errorCode;

	public ErrorResponse(String errorMessage) {
		this.errorMessage = errorMessage;
		this.errorCode = "404";
	}
	public ErrorResponse(String errorMessage, String errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
}
