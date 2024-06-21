package com.fundingflex.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fundingflex.dto.FundingsDTO;
import com.fundingflex.entity.FundingJoin;
import com.fundingflex.entity.Fundings;
import com.fundingflex.entity.Images;
import com.fundingflex.repository.FundingJoinRepository;
import com.fundingflex.repository.FundingsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundingsService {

	private final FundingsRepository fundingsRepository;
	private final FundingJoinRepository fundingJoinRepository;


	@Transactional
	public List<FundingsDTO> getAllFundings() {
	    List<Fundings> fundingsList = fundingsRepository.findAll();
	    return fundingsList.stream().map(funding -> {
	        FundingsDTO dto = new FundingsDTO();
	        dto.setFundingsId(funding.getFundings_id());
	        dto.setTitle(funding.getTitle());
	        dto.setContent(funding.getContent());
	        dto.setStatusFlag(funding.getStatusFlag()); // 추가된 부분
	        dto.setLikeCount(funding.getLikeCount());
	        dto.setGoalAmount(funding.getGoalAmount());
	        
	        int currentAmount = fundingJoinRepository.findByFundingsId(funding.getFundings_id())
	            .stream().mapToInt(FundingJoin::getFundingAmount).sum();
	        dto.setCurrentAmount(currentAmount);
	        
	        List<String> imageUrls = funding.getImageList().stream()
	            .map(Images::getImageUrl).collect(Collectors.toList());
	        dto.setImageUrls(imageUrls);
	        
	        return dto;
	    }).collect(Collectors.toList());
	}
}
