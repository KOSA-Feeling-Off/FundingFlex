package com.fundingflex.funding.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundingflex.category.domain.entity.Categories;
import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.funding.domain.dto.FundingIdsDTO;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.ImageData;
import com.fundingflex.funding.domain.dto.ResponseFundingInfoDTO;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.domain.form.FundingsForm;
import com.fundingflex.funding.service.FundingsService;
import com.fundingflex.funding.service.ImageService;
import com.fundingflex.member.domain.dto.CustomUserDetails;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/fundings")
@RequiredArgsConstructor
public class FundingsController {

    private final FundingsService fundingsService;
    private final CategoriesService categoriesService;
    private final ImageService imageService;
    

    // 펀딩 개설
    @GetMapping
    public String showFundingForm(Model model,
    		@AuthenticationPrincipal CustomUserDetails userDetails) {

    	if(userDetails == null) {
    		return "redirect:/api/login";
    	}
    	
        List<Categories> categoryList = categoriesService.selectAllCategories();
        
        model.addAttribute("fundingsForm", new FundingsForm());
        model.addAttribute("categoryList", categoryList);
        
        return "funding/funding-form";
    }
    

    // 개설 펀딩 저장
    @PostMapping
    public String createFunding(@ModelAttribute("fundingsForm") FundingsForm fundingsForm,
            @RequestParam("images") MultipartFile[] images, 
            @AuthenticationPrincipal CustomUserDetails userDetails) {
    	
    	if(userDetails == null) {
    		return "redirect:/api/login";
    	}
    	
        FundingIdsDTO fundingsDto =
        		fundingsService.saveFundings(fundingsForm, images, userDetails.getUserId());
        
        return "redirect:/api/fundings/" + fundingsDto.getCategoryId()
                    + "/details/" + fundingsDto.getFundingsId();
    }
    

    // 펀딩 수정 form
    @GetMapping("/{category-id}/{funding-id}")
    public String showFundingModifyForm(@PathVariable(name = "category-id") Long categoryId,
            @PathVariable(name = "funding-id") Long fundingId, 
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
    	
    	if(userDetails == null) {
    		return "redirect:/api/login";
    	}

        List<Categories> categoryList = categoriesService.selectAllCategories();

    	// 펀딩 정보 조회
        FundingsForm fundingsInfo =
    			fundingsService.selectFundingsForm(categoryId, fundingId);

        // 이미지 조회
        List<Images> imageList = imageService.selectImagesByFundingId(fundingId);

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("fundingsInfo", fundingsInfo);
        model.addAttribute("imageList", imageList);

        return "funding/funding-modify-form";
    }


    // 개설 펀딩 수정
    @PostMapping("/{category-id}/{funding-id}")
    public String updateFunding(
        @ModelAttribute("fundingsForm") FundingsForm fundingsForm,
        @RequestParam("images") MultipartFile[] images,
        @RequestParam("imageArray") String imageArrayJson,
        @PathVariable("category-id") Long categoryId,
        @PathVariable("funding-id") Long fundingId,
        @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

    	if(userDetails == null) {
    		return "redirect:/api/login";
    	}
    	

        // JSON 문자열을 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<ImageData> imageArray =
            objectMapper.readValue(imageArrayJson, new TypeReference<ArrayList<ImageData>>(){});

        
        FundingIdsDTO fundingsDto =
        		fundingsService.updateFunding(fundingsForm, imageArray, categoryId, fundingId);

        return "redirect:/api/fundings/" + fundingsDto.getCategoryId()
                    + "/details/" + fundingsDto.getFundingsId();
    }

    
    // 펀딩 상세 조회
    @GetMapping("/{category-id}/details/{funding-id}")
    public String getFundingDetails(@PathVariable(name = "category-id") Long categoryId,
            @PathVariable(name = "funding-id") Long fundingId, Model model) throws Exception {
        
        // 공통으로 들어올 수 있기 때문에 CustomUserDetails를 사용
        CustomUserDetails userDetails = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomUserDetails) {
            userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        Long userId = (userDetails != null) ? userDetails.getUserId() : -1L;

       ResponseFundingInfoDTO fundingsInfo =
    		   fundingsService.selectFundinsInfo(categoryId, fundingId, userId);

        model.addAttribute("loginUserId", userId);
        model.addAttribute("responseFundingInfo", fundingsInfo);
        return "funding/funding-details";
    }


    // 펀딩 목록 ajax
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<FundingsDTO>> getAllFundings(
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "userId") Long userId) {
        try {
            List<FundingsDTO> fundingsList;
            if ("inProgress".equals(sortBy)) {
                fundingsList = fundingsService.getInProgressFundings(sortBy, userId);
            } else {
                fundingsList = fundingsService.getAllFundings(sortBy, userId);
            }
            return ResponseEntity.ok(fundingsList);
        } catch (Exception e) {
            // 로그 출력
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
