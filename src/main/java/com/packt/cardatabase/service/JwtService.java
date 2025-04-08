package com.packt.cardatabase.service;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	@Value("${jwt.secret}")
	private String secretKey;
	
	static final long EXPIRATIONTIME = 86400000; // 1일을 밀리초로 계산한 값
	static final String PREFIX = "Bearer"; // 토큰의 접두사 정의
	
	// Base64 디코딩 후 SecretKey 변환
	private SecretKey getSigningKey() {
		byte[] decodedKey = Base64.getDecoder().decode(secretKey);
		return Keys.hmacShaKeyFor(decodedKey);
	}
	
	// 서명된 JWT 토큰 생성
	public String getToken(String username) {
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME)) // 현재 시간 기준으로 1일 뒤 만료되도록 설정 
				.signWith(getSigningKey()) // 비밀 키로 서명
				.compact(); // jwt를 문자열로 변환
		return token;
	}
	
	// http 요청에서 jwt를 꺼내서 검증하고 사용자 정보를 추출
	public String getAuthUser(String token) {
		if (token != null && token.startsWith(PREFIX)) { // "Bearer "로 시작하는지 확인
	        String jwt = token.replace(PREFIX, "").trim(); // "Bearer " 부분을 제거하고 공백도 처리

	        String user = Jwts.parserBuilder()
	                .setSigningKey(getSigningKey()) // 검증할 때도 같은 방식
	                .build()
	                .parseClaimsJws(jwt) // 토큰 검증
	                .getBody()
	                .getSubject();
			
			if(user != null) { // 사용자 이름이 있으면 반환
				return user;
			}
		}
		return null;
	}
    
    // 인증 객체 생성 (인증된 사용자 반환)
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String username = token;

        if (username != null) {
            return new UsernamePasswordAuthenticationToken(username, null, null); // 사용자 이름만 인증 정보로 사용
        }       
        return null;
    }

}
