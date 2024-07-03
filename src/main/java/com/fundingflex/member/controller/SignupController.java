package com.fundingflex.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.member.domain.form.MemberResisterForm;
import com.fundingflex.member.service.SignupService;

import jakarta.validation.Valid;

//@Slf4j
//@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class SignupController {
	
	@Autowired
    private SignupService signupService;

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

        if (signupService.existsByEmail(memberResisterForm.getEmail())) {
            bindingResult.rejectValue("email", "emailExists", "이미 사용 중인 이메일입니다.");
            return "pages/signup_form";
        }

        if (signupService.existsByNickname(memberResisterForm.getNickname())) {
            bindingResult.rejectValue("nickname", "nicknameExists", "이미 사용 중인 닉네임입니다.");
            return "pages/signup_form";
        }

        signupService.signup(memberResisterForm);
        return "redirect:/api/login";
    }
}