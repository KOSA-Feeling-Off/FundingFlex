package com.fundingflex.mypage.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.mypage.service.MypageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping
    public String mypage(Model model, Principal principal) {
		/*
		 * if (principal == null) { return "redirect:/api/login"; }
		 * 
		 * String email = principal.getName(); Members user =
		 * mypageService.findByEmail(email);
		 * 
		 * if (user == null) { return "redirect:/api/login"; }
		 * 
		 * Long userId = user.getUserId();
		 * 
		 * List<FundingsDTO> participatedFundings =
		 * mypageService.findParticipatedFundingsByUserId(userId); List<FundingsDTO>
		 * createdFundings = mypageService.findCreatedFundingsByUserId(userId);
		 * List<FundingsDTO> likedFundings =
		 * mypageService.findLikedFundingsByUserId(userId);
		 * 
		 * model.addAttribute("participatedFundings", participatedFundings);
		 * model.addAttribute("createdFundings", createdFundings);
		 * model.addAttribute("likedFundings", likedFundings);
		 */

        return "funding/mypage";
    }
}
