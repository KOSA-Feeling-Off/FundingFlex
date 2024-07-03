package com.fundingflex.member.controller;



import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import com.fundingflex.member.domain.dto.MyPageDTO;
import com.fundingflex.member.service.MyPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {
    private final MyPageService myPageService; 
    
    @GetMapping("/mypage")
    public String getMyProfile(@PathVariable Long userId, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long loginUserId = userDetails.getUserId();

        // 현재 로그인한 사용자의 userId와 요청된 userId 비교
        if (!userId.equals(loginUserId)) {
            // 로그인한 사용자의 userId와 요청된 userId가 다를 경우, 경고창을 띄워 잘못된 접근을 알림
            log.warn("Unauthorized access attempted for userId: {}", userId);
            return "error/403"; // 예시로 403 Forbidden 페이지를 반환
        }

        // userId가 일치하면 프로필 정보를 가져오는 로직
        MyPageDTO myProfileDTO = myPageService.getMyPage(userId);
        model.addAttribute("myProfileDTO", myProfileDTO);

        // 좋아요 누른 펀딩글 가져오기
        List<FundingsDTO> likedFundings = myPageService.getLikedFundings(userId);
        model.addAttribute("likedFundings", likedFundings);

        // 내가 작성한 펀딩글 가져오기
        List<FundingsDTO> myFundings = myPageService.getMyFundings(userId);
        model.addAttribute("myFundings", myFundings);

        // 내가 참여한 펀딩글 가져오기
        List<FundingsDTO> joinedFundings = myPageService.getJoinedFundings(userId);
        model.addAttribute("joinedFundings", joinedFundings);

        return "pages/mypage";
    }
}
