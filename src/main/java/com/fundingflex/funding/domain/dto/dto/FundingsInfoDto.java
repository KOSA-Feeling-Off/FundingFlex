package com.fundingflex.funding.domain.dto.dto;

import com.fundingflex.funding.domain.dto.enums.FundingsStatusEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingsInfoDto {

	private Long categoryId;			// 카테고리 아이디
	private String categoryName;		// 카테고리 명
	private Long fundingsId;			// 펀딩 아이디
	private String title;				// 펀딩 제목
	private String content;				// 펀딩 내용
	private int goalAmount;				// 펀딩 목표 금액
	private FundingsStatusEnum statusFlag;	// 펀딩 진행 상태
	
	// TODO: 사용자 받아와야함
}
