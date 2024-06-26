package com.fundingflex.funding.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.category.domain.dto.dto.CategoriesDto;
import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.funding.domain.dto.FundingIdsDTO;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.dto.ResponseFundingInfoDTO;
import com.fundingflex.funding.domain.form.FundingsForm;
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
        
        FundingIdsDTO fundingsDto = fundingsService.saveFundings(fundingsForm, images);
        
        return "redirect:/api/fundings/" + fundingsDto.getCategoryId()
                    + "/details/" + fundingsDto.getFundingsId();
    }
    
    // 펀딩 목록 화면 조회
    @GetMapping("/list-view")
    public String getFundingsPage() {
        return "fundings.html"; // static 폴더 내의 HTML 파일 이름
    }

    
    // 펀딩 상세 조회
    @GetMapping("/{category-id}/details/{funding-id}")
    public String getFundingDetails(@PathVariable(name = "category-id") Long categoryId,
            @PathVariable(name = "funding-id") Long fundingId, Model model) throws Exception {
        
       ResponseFundingInfoDTO fundingsInfo = fundingsService.selectFundinsInfo(categoryId, fundingId);
        model.addAttribute("responseFundingInfo", fundingsInfo);
        return "funding/funding-details";
    }

    // 펀딩 목록 ajax
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<FundingsDTO>> getAllFundings() {
        List<FundingsDTO> fundingsList = fundingsService.getAllFundings();
        return ResponseEntity.ok(fundingsList);
    }
    
    // 좋아요 처리
    @PostMapping("/like/{fundingsId}")
    @ResponseBody
    public ResponseEntity<?> likeFunding(@PathVariable("fundingsId") Long fundingsId) {
        boolean liked = fundingsService.likeFunding(fundingsId);
        return ResponseEntity.ok(Map.of("liked", liked));
    }
    
    
}
