package com.fundingflex.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.member.domain.dto.AuthResponse;
import com.fundingflex.member.domain.form.MemberLoginForm;
import com.fundingflex.member.service.SigninService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class SigninController {

    private final SigninService signinService;

    
    @GetMapping("/signin")
	public String signin(MemberLoginForm memberLoginForm) {
		return "/pages/signin_form";
	}
    
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody MemberLoginForm loginForm) {
        try {
            // SigninService를 사용하여 사용자 인증 및 토큰 생성
            String token = signinService.authenticateAndGenerateToken(loginForm);
            String bearerToken = token; // "Bearer "를 토큰 앞에 붙임
            log.info("Login successful, JWT Token: {}", bearerToken);
            // 응답에 JWT 토큰을 포함하여 반환합니다.
            return ResponseEntity.ok().header("Authorization", bearerToken).body(new AuthResponse(bearerToken)); // 변경된 부분
        } catch (AuthenticationException e) {
            // 인증에 실패하면 401 Unauthorized 응답을 반환합니다.
            return ResponseEntity.status(401).body("Authentication failed");
        }
    }
}
