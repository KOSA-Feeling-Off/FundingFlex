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

import com.fundingflex.common.util.jwt.JWTFilter;
import com.fundingflex.common.util.jwt.JWTUtil;
import com.fundingflex.common.util.jwt.LoginFilter;

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
//		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class); // 추가코드
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();// 추가코드 2
		    http.csrf(csrf -> csrf.disable())
		        .formLogin(form -> form.disable())
		        .httpBasic(basic -> basic.disable())
		        .authorizeHttpRequests(auth -> auth
//		            .requestMatchers("/api/signup", "/", "/api/signin").permitAll()
//		            .requestMatchers("/admin").hasRole("ADMIN")
		            .requestMatchers("/api/signup", "/", "/api/signin").permitAll()  // /api/signin 경로 명시적 허용
		            .anyRequest().permitAll())
//		        .addFilterBefore(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
		        .addFilterBefore(new LoginFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class)
		        .addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class)
		        .sessionManagement(session -> session
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		    return http.build();
	}
}