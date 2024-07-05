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
        if (members != null) {
            model.addAttribute("members", members);
            
         // 프로필 이미지 URL 처리
            if (members.getProfileUrl() != null && !members.getProfileUrl().isEmpty()) {
                // DB에서 가져온 URL을 웹 접근 가능 경로로 변환
                String basePath = "src/main/resources/static/images/members/";
                int basePathLength = basePath.length();
                String fileName = members.getProfileUrl().substring(basePathLength);
                members.setProfileUrl("/images/members/" + fileName);
                System.out.println("Processed profile URL: " + members.getProfileUrl());
            } else {
                // 기본 이미지 설정
                members.setProfileUrl("/images/default-profile-image.png");
            }
            model.addAttribute("profileUrl", members.getProfileUrl());
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
        
        int likedCount = myPageService.countLikedFundingsByUserId(userId);
        model.addAttribute("likedCount", likedCount);
        
        int participatedCount = myPageService.countParticipatedFundingsByUserId(userId);
        model.addAttribute("participatedCount", participatedCount);
        
        int createdCount = myPageService.countCreatedFundingsByUserId(userId);
        model.addAttribute("createdCount", createdCount);
        
        model.addAttribute("userDetails", userDetails);
        return "pages/mypage";
    }
}
