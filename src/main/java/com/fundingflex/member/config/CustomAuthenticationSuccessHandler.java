package com.fundingflex.member.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 로그인성공시 수행하는 handler
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 이전 페이지 URL을 세션에서 가져옵니다. 없다면 기본 페이지로 설정
        String redirectUrl = (String) request.getSession().getAttribute("redirectUrl");
        if (redirectUrl == null) {
            redirectUrl = "/api/home";
        }
        response.sendRedirect(redirectUrl);
	}
}
