package com.fundingflex.member.config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// 세션에 인증 실패 메시지 저장
        request.getSession().setAttribute("authError", "로그인 후 이용 가능합니다!");
        
        // 로그인 페이지로 리다이렉트
        response.sendRedirect("/api/login");
	}
}
