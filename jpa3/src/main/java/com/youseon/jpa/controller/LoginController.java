package com.youseon.jpa.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.youseon.jpa.dto.AccountDto;
import com.youseon.jpa.security.AuthenticationResponse;
import com.youseon.jpa.security.JwtUtil;
import com.youseon.jpa.service.MyUserDetailsService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @CrossOrigin("*")
    @PostMapping("/api/login")
    @ApiOperation(value = "로그인 요청")
    public ResponseEntity<?> createAuthenticationToken(
    		@RequestBody AccountDto accountDto) throws Exception {
    	System.out.println("컨트롤러 도착!");
    	System.out.println(accountDto.getId()+"와"+accountDto.getPassword());
        try {		
        			// 인증은 authenticationmanager가 담당함
            		authenticationManager.authenticate(
            				new UsernamePasswordAuthenticationToken(accountDto.getId(),accountDto.getPassword())
                    );
            
        } catch (Exception e) {
            throw new Exception("Incorrect username or password", e);
        }
        
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(accountDto.getId());
        System.out.println("userDetails::::::::"+userDetails);
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        System.out.println("jwt::::::::"+jwt);
        
        System.out.println("new AuthenticationResponse(jwt):::::::" + new AuthenticationResponse(jwt));
        
        HashMap<String, Object> returnMap = new HashMap<>();
        
        returnMap.put("role", userDetails.getAuthorities());
        returnMap.put("jwt", jwt);
        
        return ResponseEntity.ok(returnMap);
    }

}
