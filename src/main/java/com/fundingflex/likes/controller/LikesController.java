package com.fundingflex.likes.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundingflex.likes.service.LikesService;
import com.fundingflex.member.domain.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class LikesController {

	private final LikesService likesService;
	

	// 좋아요 기능
    @PostMapping("/like/{fundingsId}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> toggleLikeFunding(@PathVariable("fundingsId") Long fundingsId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            Long userId = userDetails.getUserId();
            boolean liked = likesService.toggleLikeFunding(fundingsId, userId);

            return ResponseEntity.ok(Map.of("liked", liked));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
}
