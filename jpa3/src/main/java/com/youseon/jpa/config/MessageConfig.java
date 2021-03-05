package com.youseon.jpa.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration 
public class MessageConfig {
	@Bean 
	public ResourceBundleMessageSource messageSource() {
		
		Locale.setDefault(Locale.KOREA);
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		/* message properties 위치 설정 */ 
		source.setBasenames("message/security_message", "org/springframework/security/messages"); 
		/* encoding 룰 설정 */ 
		source.setDefaultEncoding("UTF-8");
		/* 5초간 케싱 프로퍼티 파일의 변경을 감지할 시간 간격 지정*/ 
		source.setCacheSeconds(5); 
		// 없는 메세지일 경우 예외를 발생시키는 대신 코드를 기본 메세지로 한다.
        source.setUseCodeAsDefaultMessage(true);
		return source; 
	} 
}

