package com.fundingflex.funding.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;
import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.funding.domain.dto.dto.FundingsDto;
import com.fundingflex.funding.domain.dto.dto.FundingsInfoDto;
import com.fundingflex.funding.domain.dto.form.FundingsForm;
import com.fundingflex.funding.service.FundingsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class FundingsController {

	
	private final FundingsService fundingsService;
	private final CategoriesService categoriesService;
	
	
	// 펀딩 개설
	@GetMapping
	public String showFundingForm(Model model) {
		
		List<CategoriesDto> categoryList = categoriesService.selectAllCategories();
		
		model.addAttribute("fundingsForm", new FundingsForm());
		model.addAttribute("categoryList", categoryList);
		
		return "funding/funding-form";
	}

	
	// 개설 펀딩 저장
	@PostMapping
	public String createFunding(@ModelAttribute("fundingsForm") FundingsForm fundingsForm,
			@RequestParam("images") MultipartFile[] images, Model model) {
		
		FundingsDto fundingsDto = fundingsService.saveFundings(fundingsForm, images);
		
		return "redirect:/api/fundings/" + fundingsDto.getCategoryId()
					+ "/details/" + fundingsDto.getFundingsId();
	}
	
	
	// 펀딩 상세 조회
	@GetMapping("/{category-id}/details/{funding-id}")
	public String getFundingDetails(@PathVariable(name = "category-id") Long categoryId,
			@PathVariable(name = "funding-id") Long fundingId, Model model) {
		
		FundingsInfoDto fundingsInfo = fundingsService.selectFundinsInfo(categoryId, fundingId);

//	    model.addAttribute("fundingImages", fundings.getImageList());
	    model.addAttribute("fundingsInfo", fundingsInfo);

	    return "funding/funding-details";
	}
}
