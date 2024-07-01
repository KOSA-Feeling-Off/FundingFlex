package com.fundingflex.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.common.util.jwt.JWTUtil;
import com.fundingflex.member.domain.dto.AuthResponse;
import com.fundingflex.member.domain.form.MemberLoginForm;
import com.fundingflex.member.service.SigninService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class SigninController {

	private final SigninService signinService;
	private final JWTUtil jwtUtil;

	@GetMapping("/login")
	public String signin(Model model) {
		model.addAttribute("memberLoginForm", new MemberLoginForm());
		return "/pages/signin_form";
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody MemberLoginForm loginForm) {
		try {
			// SigninService를 사용하여 사용자 인증 및 토큰 생성
			String token = signinService.authenticateAndGenerateToken(loginForm);
			log.info("Login successful, JWT Token: {}", token);
			return ResponseEntity.ok().body(new AuthResponse(token)); // Authorization 헤더 대신 응답 본문에 포함시키기.
			// 응답에 JWT 토큰을 포함하여 반환합니다.
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
		}
	}
}
