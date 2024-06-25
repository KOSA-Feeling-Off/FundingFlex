package com.fundingflex.member.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fundingflex.member.domain.dto.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		// HttpServletRequest에서 사용자 이름과 비밀번호를 추출합니다.
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		log.info(" " + username + "," + password + " ");
		// 사용자 이름과 비밀번호로 인증 토큰을 생성합니다.
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,
				null);
		// AuthenticationManager를 통해 인증을 시도합니다.
		return authenticationManager.authenticate(authToken);
	}

	// 로그인 성공시 JWT를 발급
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,	
			Authentication authentication) {
		log.info(" success ");

		// CustomUserDetails 객체를 가져옵니다. 이는 인증된 사용자 정보를 포함합니다.
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		// 사용자 이름을 CustomUserDetails 객체에서 가져옵니다.
		String email = customUserDetails.getUsername(); // email
		// 인증된 사용자의 권한(roles)을 가져옵니다.
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		// 첫 번째 권한의 이름(역할)을 가져옵니다.
		String role = auth.getAuthority();
		// JWT 토큰을 생성합니다. 토큰은 사용자 이름과 역할, 유효 기간을 포함합니다.
		String token = jwtUtil.createJwt(email, role, 60 * 60 * 10L); // 10시간
		// 생성된 JWT 토큰을 HTTP 응답 헤더에 추가합니다.
		response.addHeader("Authorization", "Bearer " + token);
	}

	// 로그인 실패시 처리
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) {
		log.info(" failure ");
		response.setStatus(401);
	}
}
