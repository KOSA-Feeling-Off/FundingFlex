package com.fundingflex.mypage.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.mypage.domain.dto.MyPageDTO;
import com.fundingflex.mypage.service.MyPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {
    private final MyPageService myPageService; 
    
    @GetMapping("/mypage")
    public String getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
    	long userId = userDetails.getUserId();
    	
    	MyPageDTO members = myPageService.findMemberInfoByUserId(userId);
    	if(members != null) {
    		model.addAttribute("members", members);
    		
    		// Handle profile URL if null
            String profileUrl = members.getImageUrl() != null ? members.getImageUrl() : "/images/default-profile-image.png";
            model.addAttribute("profileUrl", profileUrl);
    	} else {
            log.error("User with userId {} not found", userId);
            return "error";
    	}
        model.addAttribute("userDetails", userDetails);
        
        // 좋아요 누른 펀딩글 조회
        List<FundingsDTO> likedFundings = myPageService.findLikedFundingsByUserId(userId);
        model.addAttribute("likedFundings", likedFundings);
        
        // 사용자가 참여한 펀딩 정보 조회
        List<FundingsDTO> participatedFundings = myPageService.findParticipatedFundingsByUserId(userId);
        model.addAttribute("participatedFundings", participatedFundings);
        
        // 내가 만든 펀딩글 조회
        List<FundingsDTO> createdFundings = myPageService.findCreatedFundingsByUserId(userId);
        model.addAttribute("createdFundings", createdFundings);
        
        model.addAttribute("userDetails", userDetails);
        return "pages/mypage";
    }
}
