package com.packt.cardatabase;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.packt.cardatabase.service.JwtService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends OncePerRequestFilter{
	private final JwtService jwtService;
	
	public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = jwtService.getAuthUser(token);
        
        if (jwt != null) {
            // 인증된 사용자 설정
            Authentication jwtAuth = jwtService.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(jwtAuth);
        }

        filterChain.doFilter(request, response); // 필터 체인 계속 진행
    }

}

