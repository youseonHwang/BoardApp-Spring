package com.youseon.jpa.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Java의 Serialize는 치명적인 문제를 가지고 있습니다.
 * Serialize한 Object나 Data를 Deserialize할 때, Serialize와 Deserialize하는 시스템이 다른 경우(ex. 버전문제) 에러(InvalidClassException)가 생성
 * 따라서 시스템의 버전의 호환성을 유지하기 위해서는 serialVersionUID가 필요
 * @author youseon
 *
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    
    @Override
    public void commence(HttpServletRequest request, 
    					 HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
    	
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, authException);
            os.flush();
        }
    }
}
