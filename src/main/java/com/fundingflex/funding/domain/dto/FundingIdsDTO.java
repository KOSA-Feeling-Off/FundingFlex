package com.fundingflex.funding.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FundingIdsDTO {

   private Long categoryId;
   private Long fundingsId;
}
