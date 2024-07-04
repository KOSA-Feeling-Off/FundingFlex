package com.fundingflex.funding.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FundingResponseDTO {
    private Long fundingJoinId;
}
