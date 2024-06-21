package com.fundingflex.funding.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fundingflex.category.service.CategoriesService;
import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.dto.dto.FundingsDto;
import com.fundingflex.funding.domain.dto.dto.FundingsInfoDto;
import com.fundingflex.funding.domain.dto.enums.FundingsStatusEnum;
import com.fundingflex.funding.domain.dto.form.FundingsForm;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.repository.FundingsRepository;
import com.fundingflex.funding.repository.ImagesRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingsService {

	private final FundingsRepository fundingsRepository;
	private final ImagesRepository imagesRepository;
	private final CategoriesService categoriesService;
	
	
	
	// 이미지 파일 경로
	private final Path fundingImgPath = Paths.get("src/main/resources/static/images/fundings");
	
	
	// 저장
	@Transactional
	public FundingsDto saveFundings(FundingsForm fundingsForm, MultipartFile[] images) {
		
		// 폴더 경로 생성
	    if (!Files.exists(fundingImgPath)) {
	    	
	        try {
	            Files.createDirectories(fundingImgPath);
	            
	        } catch (IOException ex) {
	            throw new RuntimeException("폴더 생성 실패: " + ex.getMessage());
	        }
	    }
	    
	    
	    // Fundings 객체 생성
	    Fundings newFundings = Fundings.builder()
	            .categoryId(fundingsForm.getCategoryId())
//	            .categoryName(categoriesService.get)
	            
	            .title(fundingsForm.getTitle())
	            .content(fundingsForm.getContent())
	            .goalAmount(fundingsForm.getGoalAmount())
	            .statusFlag(FundingsStatusEnum.WAITING)
	            .isDeleted(DeleteFlagEnum.N)
	            .build();
		
	    
	    // 이미지 처리 및 Images 객체 생성
	    int idx = 1;		// 파일 순서
	    List<Images> imageList = new ArrayList<>();
	    try {
	        for (MultipartFile file : images) {
	        	
	            if (!file.isEmpty()) {
	                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	                Files.copy(file.getInputStream(), this.fundingImgPath.resolve(fileName));

	                Images newImage = Images.builder()
	                        .imageUrl(fileName)
	                        .fundings(newFundings)
	                        .seq(idx++)
	                        .isDeleted(DeleteFlagEnum.N)
	                        .build();

	                imageList.add(newImage);
	            }
	        }
	        
	    } catch (IOException ex) {
	        throw new RuntimeException("이미지 처리 실패: " + ex.getMessage());
	    }
		
	    
	    // Fundings 객체에 이미지 리스트 설정
	    newFundings.setImageList(imageList);

	    // Fundings 객체와 Images 객체 저장
	    fundingsRepository.save(newFundings);
	    imagesRepository.saveAll(imageList);
	    
	    return FundingsDto.builder()
	    		.categoryId(newFundings.getCategoryId())
	    		.fundingsId(newFundings.getFundingsId())
	    		.build();
	}

	
	public FundingsInfoDto selectFundinsInfo(Long fundingId, Long categoryId) {
		return null;
	}
	

	// fundingId 기준 조회
	public Fundings findById(Long fundingId) {
		return fundingsRepository.findById(fundingId)
				.orElseThrow(() -> new EntityNotFoundException("펀딩 데이터가 존재하지 않습니다."));
	}
	
}
