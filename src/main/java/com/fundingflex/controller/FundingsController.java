package com.fundingflex.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundingflex.dto.FundingsDTO;
import com.fundingflex.service.FundingsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundingsController {

	private final FundingsService fundingsService;


	@GetMapping("/api/fundings")
	@ResponseBody
	public ResponseEntity<List<FundingsDTO>> getAllFundings() {
	    List<FundingsDTO> fundingsList = fundingsService.getAllFundings();
	    return ResponseEntity.ok(fundingsList);
	}


	@GetMapping("/fundings")
	public String getFundingsPage() {
		return "fundings.html"; // static 폴더 내의 HTML 파일 이름
	}
}
