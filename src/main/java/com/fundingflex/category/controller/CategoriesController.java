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

import com.fundingflex.category.domain.entity.Categories;
import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.service.FundingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/categories")
public class CategoriesController {

	/*
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(CategoriesController.class);
	 */

    private final FundingsService fundingsService;
    private final CategoriesService categoriesService;

    // 카테고리별 펀딩 목록 화면 조회
    @GetMapping("/{categoryId}")
    public String getCategoryFundingsPage(@PathVariable("categoryId") Long categoryId, Model model) {
        Categories category = categoriesService.selectCategoriesById(categoryId);
        
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categoryName", category.getCategoryName());
        
        return "category/category-list";
    }

    // 카테고리별 펀딩 목록 ajax
    @GetMapping("/{categoryId}/list")
    @ResponseBody
    public ResponseEntity<List<FundingsDTO>> getCategoryFundings(@PathVariable("categoryId") Long categoryId,
                                                                 @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
        log.debug("=>>>>>>>>>>>>>> Fetching fundings for category ID: {}", categoryId);
        List<FundingsDTO> fundingsList = fundingsService.getFundingsByCategory(categoryId, sortBy);
        
        log.debug("=>>>>>>>>>>>>>> Fetched fundings: {}", fundingsList);
        return ResponseEntity.ok(fundingsList);
    }
}
