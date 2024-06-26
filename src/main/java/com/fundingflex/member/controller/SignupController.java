package com.fundingflex.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.member.domain.form.MemberLoginForm;
import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.member.service.SignupService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class SignupController {

	private final SignupService memberService;

	// 회원가입
	@GetMapping("/signup")
	public String signup(MemberResisterForm memberResisterForm) {
		return "pages/signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid MemberResisterForm memberResisterForm, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "pages/signup_form";
		}

		if (!memberResisterForm.getPassword1().equals(memberResisterForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "pages/signup_form";
		}

		if (memberService.existsByEmail(memberResisterForm.getEmail())) {
			bindingResult.rejectValue("email", "emailExists", "이미 사용 중인 이메일입니다.");
			return "pages/signup_form";
		}

		if (memberService.existsByNickname(memberResisterForm.getNickname())) {
			bindingResult.rejectValue("nickname", "nicknameExists", "이미 사용 중인 닉네임입니다.");
			return "pages/signup_form";
		}

		memberService.signup(memberResisterForm);
		return "redirect:/";
	}
	//
//	@PostMapping("/signin")
//    public String signin(@Valid MemberLoginRequestDTO dto, BindingResult bindingResult, HttpServletResponse response) {
//        if (bindingResult.hasErrors()) {
//            return "pages/signin_form";
//        }
//
//        String email = dto.getEmail();
//        String password = dto.getPassword();
//
//        log.info("로그인 시도: {}", email);
//
//        try {
//            JWToken token = memberService.signin(email, password);
//            log.info("JWT 토큰 발행: accessToken = {}, refreshToken = {}", token.getAccessToken(), token.getRefreshToken());
//
//            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());
//            log.info("응답 헤더에 JWT 토큰 설정 완료");
//
//            return "redirect:/";
//        } catch (Exception e) {
//            log.error("로그인 실패", e);
//            return "redirect:/signin?error";
//        }
//    }
}
