package com.fundingflex.member.controller;

import java.security.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user/info")
    public String userInfo(Principal principal) {
        if (principal != null) {
            return "Current authenticated user: " + principal.getName();
        }
        return "No authenticated user";
    }
    
    @GetMapping("/user/infos")
    public String userInfo(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return "Current authenticated user: " + userDetails.getUsername();
        }
        return "No authenticated user";
    }
}