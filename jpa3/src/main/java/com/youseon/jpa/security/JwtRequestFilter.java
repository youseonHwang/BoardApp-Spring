package com.youseon.jpa.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.youseon.jpa.service.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Client의 Request를 Intercept해서 Header의 Token가 유효한지 검증
 * @author youseon
 **/
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	
    	System.out.println("doFilterInternal 입장!!");
    	
        final String authorizationHeader = request.getHeader("Authorization"); // 헤더값 확인
        System.out.println("authorizationHeader::::"+ authorizationHeader);
        System.out.println("doFilterInternal입니다. 여기로 오나요?");
        String username= null;
        String jwt = null;
        
        // 헤더가 있는경우
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
        	
            jwt = authorizationHeader.substring(7);
            System.out.println(jwt);
            username = jwtUtil.extractUsername(jwt);
            System.out.println(username);
            System.out.println("doFilterInternal입니다. 여기로 오나요? 222222");
        }
        
        // 헤더값이 있는 경우 username을 찾았다면 여기로
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            
        	UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("doFilterInternal입니다. 여기로 오나요? 333333");
            if(jwtUtil.validateToken(jwt, userDetails)){ //토큰 유효성 검사
            	
            	//AuthenticationManager(구현체인 ProviderManager)에게 인증을 진행하도록 위임
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        System.out.println("doFilterInternal입니다. 여기로 오나요? 555555");
        
        chain.doFilter(request,response); // 다음 필터에 체인처럼 연결함. 
    }
}