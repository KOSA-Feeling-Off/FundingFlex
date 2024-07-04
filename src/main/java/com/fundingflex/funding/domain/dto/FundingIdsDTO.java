package com.fundingflex.funding.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingIdsDTO {

   private Long categoryId;
   private Long fundingsId;
   
   
   public static FundingIdsDTO of(Long categoryId, Long fundingsId) {
	   return FundingIdsDTO.builder()
			   .categoryId(categoryId)
			   .fundingsId(fundingsId)
			   .build();
   }
}
