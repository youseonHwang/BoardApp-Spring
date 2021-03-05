package com.youseon.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.youseon.jpa.security.JwtAuthenticationEntryPoint;
import com.youseon.jpa.security.JwtRequestFilter;
import com.youseon.jpa.service.MyUserDetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	// 정적 컨텐츠에 대해 인증을 무시하도록 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
              web.ignoring().antMatchers(
            		 "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", // swgger 사용시
                     "/index.html",   // front-end 에서 build한 static file
                     "/favicon.ico",   // 여기서 설정 안 해주면 index.html이 읽을 수 없음
                     "/css/**",   // 여기서 설정 안 해주면 index.html이 읽을 수 없음
                     "/fonts/**",   // 여기서 설정 안 해주면 index.html이 읽을 수 없음
                     "/img/**",   // 여기서 설정 안 해주면 index.html이 읽을 수 없음
                     "/js/**"   // 여기서 설정 안 해주면 index.html이 읽을 수 없음
              );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        		.csrf().disable()
        		.formLogin()
	        		.usernameParameter("id") // username값을 'id'로 사용
	        		.loginPage("http://localhost:8080/") // 앞으로 로그인은 이 경로에서 수행하게 된다는 뜻이다.
//	        		.failureHandler(authFail)
//	        		.successHandler(authSuccess)
	        		.and()
                .authorizeRequests()
	                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll() // //OPTIONS 메소드 허락 (CORS때문)
	                .antMatchers("/api/login").permitAll() // login요청 모두 허용
	                .antMatchers("/error").permitAll() // error페이지 모두 허용 
	                .antMatchers("/chart").permitAll() // chart는 모두 허용
	                .antMatchers("/review").permitAll() // review 모두 허용
	                .antMatchers("/api/count").permitAll() //count 일단 다 허용
	                .antMatchers("/api/count/**").permitAll()
	                .antMatchers("/api/board").hasRole("ADMIN") // 게시판은 ADMIN전용
	                .antMatchers("/api/board/**").hasRole("ADMIN")
	                .anyRequest().authenticated()
	                .and() // 예외 처리
                .exceptionHandling()
                	.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                	.and() // 세션 처리
                .sessionManagement()
                	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 이전 필터
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    // cors 설정
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods(HttpMethod.OPTIONS.name(), HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name())
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600); // 3600초 동안 preflight 결과를 캐시에 저장
            }
        };
    }

}
