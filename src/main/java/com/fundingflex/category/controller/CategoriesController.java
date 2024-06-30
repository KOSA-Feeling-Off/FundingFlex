package com.fundingflex.category.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.service.FundingsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/categories")
public class CategoriesController {

	private final FundingsService fundingsService;
	
	// 카테고리별 펀딩 목록 화면 조회
    @GetMapping("/{categoryId}")
    public String getCategoryFundingsPage(@PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return "/category/category-list.html";
    }

    // 카테고리별 펀딩 목록 ajax
    @GetMapping("/{categoryId}/list")
    @ResponseBody
    public ResponseEntity<List<FundingsDTO>> getCategoryFundings(@PathVariable("categoryId") Long categoryId,
                                                                 @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
        List<FundingsDTO> fundingsList = fundingsService.getFundingsByCategory(categoryId, sortBy);
        return ResponseEntity.ok(fundingsList);
    }
}
