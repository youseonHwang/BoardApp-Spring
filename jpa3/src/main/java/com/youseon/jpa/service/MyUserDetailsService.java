package com.youseon.jpa.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.youseon.jpa.entity.Account;
import com.youseon.jpa.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/**
 * DB에서 UserDetail를 얻어와 AuthenticationManager에게 제공하는 역할
 * DB에 bcryptEncoder화 된 암호가 있다고 가정한 경우에만 사용가능 ∴ 회원가입 로직이 없다면 db에 수기로 암호화해서 넣어놓을것. 안그러면 작동이 안됨미더
 * @author youseon
 *
 */
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService { //userDetailService 구현체는 Repository를 통해 영속성으로 저장된 인증정보 검색
	
	private final AccountRepository accountRepository;
	
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    	Account account = accountRepository.findAccountById(id);
    	if(account==null) {
            throw new UsernameNotFoundException(id);
        }
    	System.out.println("UserDetailService 통과~~");
    	
        return account;
    }
}
