package com.packt.cardatabase.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.cardatabase.domain.AccountCredentials;
import com.packt.cardatabase.service.JwtService;

@RestController
@RequestMapping("/login")
public class LoginController { // 사용자의 로그인 요청을 처리하는 역할

	private final JwtService jwtService;

	// Spring Security의 인증 매니저(사용자가 입력한 아이디/비밀번호가 맞는지 검증한다.)
	private final AuthenticationManager authenticationManager;

	public LoginController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        System.out.println("로그인 컨트롤러 접근");
    }
	
	// 토큰을 생성하고 응답의 Authorization 헤더로 보냄
	@PostMapping
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) { // 요청 본문에서 사용자의 아이디/비밀번호를 JSON 형태로 받음 
		System.out.println("POST /login 요청 도착");
		// Spring Security에서 제공하는 로그인 정보 객체
		UsernamePasswordAuthenticationToken creds =
				new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
		
		Authentication loginAuth = authenticationManager.authenticate(creds); // authenticationManager가 사용자의 아이디와 비밀번호를 검증 후 auth 객체 반환
		// 토큰 생성
		String token = jwtService.getToken(loginAuth.getName());
		
		// 생성된 토큰(jwt)을 응답의 헤더에 추가하여 클라이언트에 반환
		return ResponseEntity.ok()
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization") // 클라이언트가 Authorization 헤더를 읽을 수 있도록 CORS 설정
				// 기본적으로 브라우저에서 Authorization 헤더를 차단하는 경우가 있어서 클라이언트에서 접근할 수 있도록 설정하는 것
				.build();
	}
}
