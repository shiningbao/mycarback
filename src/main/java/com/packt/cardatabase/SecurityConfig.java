package com.packt.cardatabase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.packt.cardatabase.service.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtService jwtService;	

	private final AuthEntryPoint exceptionHandler;
	
    public SecurityConfig(JwtService jwtService, AuthEntryPoint exceptionHandler) {
        this.jwtService = jwtService;
        this.exceptionHandler = exceptionHandler;
        System.out.println("SecurityConfig 접근");
    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (REST API용)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // 로그인 엔드포인트는 인증 없이 허용
            	.anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
            )
            .exceptionHandling(exception -> exception.authenticationEntryPoint(exceptionHandler)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 없이 인증(세션을 사용하지 않는 jwt 기반 인증을 적용)
            )
            .addFilterBefore(new AuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
        	.addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class);
        	
        return http.build();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    // CORS 설정을 위한 CorsConfigurationSource Bean 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 적용

        return source;
    }

    // CORS 필터를 Bean으로 등록
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

}
