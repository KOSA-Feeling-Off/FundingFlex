package com.fundingflex.funding.controller;

import com.fundingflex.funding.domain.dto.FundingRequestDTO;
import com.fundingflex.funding.domain.dto.FundingResponseDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.service.FundingJoinService;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/fundings")
public class FundingJoinController {

	private static final Logger log = LoggerFactory.getLogger(FundingJoinController.class);
	private final FundingJoinService fundingJoinService;

	
	
	// 펀딩 참여 form 호출
	@GetMapping("/{fundings-id}/fundingJoin")
	public String getFundingJoinPage(@PathVariable("fundings-id") Long fundingsId,
			Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		if(userDetails == null) {
    		return "redirect:/api/login";
    	}
		
		model.addAttribute("fundingsId", fundingsId);
		model.addAttribute("joinUserId", userDetails.getUserId());
		
        return "funding/fundingJoin";
    }
	
	
	// 펀딩 참여하기
	@PostMapping("/join")
	@ResponseBody
	public ResponseEntity<Object> joinFunding(@RequestBody FundingRequestDTO requestDto,
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			
			if(userDetails == null) {
				return ResponseEntity.status(401).body("");
	    	}
			
			FundingResponseDTO responseDto = fundingJoinService.joinFunding(requestDto);
		
			return ResponseEntity.ok(responseDto);
			
		} catch (IllegalArgumentException e) {
			log.error(">>>>>>>>>>>> IllegalArgumentException: {}", e.getMessage());
			return ResponseEntity.status(400).body(e.getMessage());
			
		} catch (Exception e) {
			log.error(">>>>>>>>>>>> Exception: {}", e.getMessage());
			return ResponseEntity.status(500).body("Failed to join funding: " + e.getMessage());
		}
	}
	

	// 펀딩 참여 수정하기
	@PutMapping("/join-update")
	@ResponseBody
	public ResponseEntity<Object> updateFunding(@RequestBody FundingRequestDTO requestDto) {
		try {
			FundingResponseDTO responseDto = fundingJoinService.updateFunding(requestDto);
			return ResponseEntity.ok(responseDto);
			
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(400).body(e.getMessage());
			
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	
	// 펀딩 참여 취소(삭제) 하기
	@DeleteMapping("/api/fundings/delete/{fundingJoinId}")
	@ResponseBody
	public ResponseEntity<Object> deleteFunding(@PathVariable("fundingJoinId") Long fundingJoinId) {
		try {
			fundingJoinService.deleteFunding(fundingJoinId);
			return ResponseEntity.ok().build();
			
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	
	// 펀딩 자금 조달 조회
	@GetMapping("/conditions/{fundingsId}")
	@ResponseBody
	public ResponseEntity<Object> getFundingConditions(@PathVariable Long fundingsId) {
		Optional<FundingConditions> fundingConditions = fundingJoinService.getFundingConditions(fundingsId);
		if (fundingConditions.isPresent()) {
			return ResponseEntity.ok(fundingConditions.get());
			
		} else {
			return ResponseEntity.status(404).body("Funding conditions not found");
		}
	}
}
