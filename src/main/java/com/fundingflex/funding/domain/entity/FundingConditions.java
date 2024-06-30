package com.fundingflex.funding.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundingConditions {
    private Long fcId;
    private Long fundingsId;
    private int collectedAmount;
    private int percent;

    private int goalAmount;

    public static FundingConditions of(Long fundingsId, int collectedAmount, int percent, int goalAmount) {
        return FundingConditions.builder()
                .fundingsId(fundingsId)
                .collectedAmount(collectedAmount)
                .percent(percent)
                .goalAmount(goalAmount)
                .build();
    }

}