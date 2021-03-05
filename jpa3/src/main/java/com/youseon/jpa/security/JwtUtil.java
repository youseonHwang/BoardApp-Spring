package com.youseon.jpa.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT생성하고 검증하는 역할 수행
 * @author youseon
 */
@Component
public class JwtUtil {
	
    @Value("${jwt.secret}")
    private String SECRET_KEY; // application.properties에 명시해놓은 value를 가져옴!

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    /** Token 생성 - claim:Token에 담을 정보, issuer:Token 발급자, subject: Token 제목, issuedate: Token 발급 시간
     * 				expiration: 만료시간(5*60*60=5시간), signWith:알고리즘,비밀키
     **/
    private String createToken(Map<String, Object> claims, String subject) {
    	
    	System.out.println("createToken");
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        
        System.out.println("validateToken::"+(username.equals(userDetails.getUsername()) && !isTokenExpired(token)));
        
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
