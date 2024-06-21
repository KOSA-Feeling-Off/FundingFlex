package com.fundingflex.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.member.service.MemberService;
import com.fundingflex.member.vaildation.MemberLoginForm;
import com.fundingflex.member.vaildation.MemberResisterForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class MemberController {
	
	private final MemberService memberService;

	// 회원가입
	@GetMapping("/signup")
	public String signup (MemberResisterForm memberResisterForm) {
		return "pages/signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(@Valid MemberResisterForm memberResisterForm, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "pages/signup_form";
		}
		
		if(!memberResisterForm.getPassword1().equals(memberResisterForm.getPassword2())) {
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

		
        memberService.signup(memberResisterForm.getEmail(), memberResisterForm.getNickname(), memberResisterForm.getPassword1());
		return "redirect:/";
	}

	// 로그인
		@GetMapping("/signin")
		public String signin(MemberLoginForm memberLoginForm) {
			return "/pages/signin_form";
		}
}
