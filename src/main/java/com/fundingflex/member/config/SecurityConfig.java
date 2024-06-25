package com.fundingflex.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fundingflex.member.jwt.JWTFilter;
import com.fundingflex.member.jwt.JWTUtil;
import com.fundingflex.member.jwt.LoginFilter;

import lombok.RequiredArgsConstructor;

@Configuration // Spring Bean Configuration 클래스 선언.
@EnableWebSecurity // Spring Security를 활성화합니다.
@RequiredArgsConstructor
public class SecurityConfig {
	//AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;

	/**
	 * BCryptPasswordEncoder는 비밀번호를 해싱하는 데 사용됩니다. BCrypt 해시 함수는 각 해시마다 고유한 솔트를 생성하여
	 * 보안을 강화합니다.
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//AuthenticationManager Bean 등록
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		// CSRF 보호를 비활성화
//		http.csrf((auth) -> auth.disable());
//		//기본 폼 로그인을 비활성화. JWT를 사용.
//		http.formLogin((auth) -> auth.disable());
//		// HTTP 기본 인증을 비활성화. JWT를 사용하여 인증을 처리.
//		http.httpBasic((auth) -> auth.disable());
//		// URL 패턴에 따른 접근 제어를 설정.
//		http.authorizeHttpRequests((auth) -> auth
//
//				// /signup, /, /signin URL은 인증 없이 접근을 허용.
//				.requestMatchers("/api/signup", "/", "/api/signin").permitAll()
//
//				// /admin URL은 ADMIN 역할이 있는 사용자만 접근을 허용.
//				.requestMatchers("/admin").hasRole("ADMIN")
//				// 그 외 모든 요청에 대하여 인증 요구.
//				.anyRequest().authenticated());
//
//		http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
//		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
//				UsernamePasswordAuthenticationFilter.class);
//		// sessionManagement()는 세션 관리 설정.
//		http.sessionManagement((session) -> session
//				// 세션을 사용하지 않고, 무상태(stateless)로 설정.
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//		return http.build();
//		// 설정된 HttpSecurity 객체를 빌드하여 SecurityFilterChain 객체를 반환
		
		
		
//		 // CSRF 보호를 비활성화
//	    http.csrf((csrf) -> csrf.disable());
//	    
//	    // 기본 폼 로그인을 비활성화. JWT를 사용.
//	    http.formLogin((form) -> form.disable());
//	    
//	    // HTTP 기본 인증을 비활성화. JWT를 사용하여 인증을 처리.
//	    http.httpBasic((basic) -> basic.disable());
//	    
//	    // URL 패턴에 따른 접근 제어를 설정.
//	    http.authorizeHttpRequests((auth) -> auth
//	        // /api/signup, /, /api/signin URL은 인증 없이 접근을 허용.
//	        .requestMatchers("/api/signup", "/", "/api/signin").permitAll()
//	        // /admin URL은 ADMIN 역할이 있는 사용자만 접근을 허용.
//	        .requestMatchers("/admin").hasRole("ADMIN")
//	        // 그 외 모든 요청에 대하여 인증 요구.
//	        .anyRequest().authenticated());
//	    
//	    // JWTFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
//	    http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//	    
//	    // LoginFilter를 JWTFilter 앞에 추가
//	    http.addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), JWTFilter.class);
//	    
//	    // sessionManagement()는 세션 관리 설정.
//	    http.sessionManagement((session) -> session
//	        // 세션을 사용하지 않고, 무상태(stateless)로 설정.
//	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//	    
//	    // 설정된 HttpSecurity 객체를 빌드하여 SecurityFilterChain 객체를 반환
//	    return http.build();
		
		    http.csrf(csrf -> csrf.disable())
		        .formLogin(form -> form.disable())
		        .httpBasic(basic -> basic.disable())
		        .authorizeHttpRequests(auth -> auth
//		            .requestMatchers("/api/signup", "/", "/api/signin").permitAll()
//		            .requestMatchers("/admin").hasRole("ADMIN")
		            .anyRequest().permitAll())
		        .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
		        .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), JWTFilter.class)
		        .sessionManagement(session -> session
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		    return http.build();
	}
}