package com.fundingflex.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	public String signin(Model model, HttpServletRequest request) {
		// 세션에서 에러 메시지를 가져옴
	    String errorMessage = (String) request.getSession().getAttribute("error");
	    model.addAttribute("error", errorMessage);
	    request.getSession().removeAttribute("error"); // 에러 메시지 처리 후 세션에서 삭제
		model.addAttribute("memberLoginForm", new MemberLoginForm());
		return "/pages/signin_form";
	}

	@PostMapping("/signin")
	public String authenticateUser(@Valid MemberLoginForm loginForm, BindingResult bindingResult, Model model) {
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("memberLoginForm", loginForm);
	        return "pages/signin_form";

	    }
	    try {
	        String token = signinService.authenticateAndGenerateToken(loginForm);
	        log.info("Login successful, JWT Token: {}", token);
	        model.addAttribute("token", token); // 성공 토큰을 모델에 추가
	        return "redirect:/"; // 로그인 성공 시 리다이렉션
	    } catch (AuthenticationException e) {
	        model.addAttribute("errorMessage", "Authentication failed: " + e.getMessage()); // 실패 메시지를 모델에 추가
	        return "pages/signin_form";

	    }
	}
}
