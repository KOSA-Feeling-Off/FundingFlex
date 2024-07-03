package com.fundingflex.likes.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundingflex.likes.service.LikesService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class LikesController {

	private final LikesService likesService;
	
    // 좋아요 처리
	@PostMapping("/like/{fundingsId}")
	@ResponseBody
	public ResponseEntity<Map<String, Boolean>> toggleLikeFunding(@PathVariable("fundingsId") Long fundingsId, @RequestParam("userId") Long userId) {
	    try {
	        boolean liked = likesService.toggleLikeFunding(fundingsId, userId);
	        return ResponseEntity.ok(Map.of("liked", liked));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	
}
