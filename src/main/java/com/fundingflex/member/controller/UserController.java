package com.fundingflex.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundingflex.member.domain.dto.CustomUserDetails;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/current-user")
    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "No user is currently authenticated";
        }

        return "Authenticated user: " + authentication.getName();
    }
    
    @GetMapping("/member/me")
    public Long getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getUserId();
    }
}