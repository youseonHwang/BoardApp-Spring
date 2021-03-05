package com.youseon.jpa.dto;

import com.youseon.jpa.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

	private String id;
	private String password;
	private String username;
	private String role;

	public Account toEntity() {
		Account account = Account.builder()
				.id(id)
				.password(password)
				.username(username)
				.role(role)
				.build();
		return account;
	}
}