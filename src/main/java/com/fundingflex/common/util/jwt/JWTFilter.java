package com.fundingflex.common.util.jwt;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.entity.Members;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
public class JWTFilter extends OncePerRequestFilter {
	private final JWTUtil jwtUtil; // Constructor Injection

//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// request에서 Authorization 헤더를 찾음
//		String authorization = request.getHeader("Authorization");
//		// Authorization 헤더 검증
//		if (authorization == null || !authorization.startsWith("Bearer ")) {
//			// Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 다음 필터로 넘어감
//			log.info(" token null ");
//
//			filterChain.doFilter(request, response);
//			return; // 조건이 해당되면 메소드 종료 (필수)
//		}
//		log.info(" authorization now ");
//		// "Bearer " 부분 제거 후 순수 토큰만 획득
//		String token = authorization.split(" ")[1];
//		log.info(" " + token + " : token");
//
//		// 토큰 소멸 시간 검증
//		if (jwtUtil.isExpired(token)) {
//			// 토큰이 만료되었으면 다음 필터로 넘어감
//			log.info(" token expired ");
//			filterChain.doFilter(request, response);
//			return; // 조건이 해당되면 메소드 종료 (필수)
//		}
//		// 토큰에서 username과 role 획득
//		String email = jwtUtil.getEmail(token);
//		String role = jwtUtil.getRole(token);
//		// userEntity를 생성하여 값 설정
//		Members members = new Members();
//		members.setEmail(email);
//		members.setRole(role);
//		members.setPassword("temppassword"); // 임시 비밀번호 설정
//		members.setRole(role);
//		// UserDetails에 회원 정보 객체 담기
//		CustomUserDetails customUserDetails = new CustomUserDetails(members);
//		// 스프링 시큐리티 인증 토큰 생성
//		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
//				customUserDetails.getAuthorities());
//		// 세션에 사용자 등록
//		SecurityContextHolder.getContext().setAuthentication(authToken);
//		// 다음 필터로 요청과 응답을 전달
//		filterChain.doFilter(request, response);
//	}
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("@@@@@@@@@@@@@@@@@@@@@@Authorization header value: {}", authorization); // 추가된 로그

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("Authorization header is missing or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7); // "Bearer " 이후의 토큰 부분만 추출
        log.info("Extracted Token: {}", token);

        if (jwtUtil.isExpired(token)) {
            log.info("Token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);

        Members members = new Members();
        members.setEmail(email);
        members.setRole(role);
//        members.setPassword("temppassword");

        CustomUserDetails customUserDetails = new CustomUserDetails(members);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}