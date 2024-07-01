package com.fundingflex.funding.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/check-login")
    @ResponseBody
    public ResponseEntity<Object> checkLogin(@AuthenticationPrincipal UserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        } else {
            return ResponseEntity.ok(currentUser);
        }
    }
}
