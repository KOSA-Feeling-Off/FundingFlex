package com.fundingflex.funding.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundingConditions {
	
    private Long fcId;					// 자금조달 아이디
    private Long fundingsId;			// 펀딩 아이디
    private int collectedAmount;		// 펀딩 모은 금액
    private int percent;				// 펀딩 퍼센트
    private int goalAmount;				// 펀딩 목표 금액
    private char hundredPercentFlag;	// 100% 여부

   

    public static FundingConditions of(Long fundingsId, int collectedAmount, int percent, int goalAmount) {
        return FundingConditions.builder()
                .fundingsId(fundingsId)
                .collectedAmount(collectedAmount)
                .percent(percent)
                .goalAmount(goalAmount)
                .build();
    }

}