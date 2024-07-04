package com.fundingflex.member.config;

import java.io.IOException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPointFilter extends OncePerRequestFilter {

	private final AuthenticationEntryPoint entryPoint;

	public CustomAuthenticationEntryPointFilter(AuthenticationEntryPoint entryPoint) {
		this.entryPoint = entryPoint;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (RuntimeException e) {
			// 인증 실패 처리
			entryPoint.commence(request, response, null);
		}
	}

}
