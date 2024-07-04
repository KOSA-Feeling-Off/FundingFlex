package com.fundingflex.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	
	@GetMapping("/")
    public String redirectToHome() {
        return "redirect:/api/home";
    }
	
	// 메인 화면 조회
    @GetMapping("/api/home")
    public String getFundingsPage() {
        return "/funding/fundings"; 
    }
}
