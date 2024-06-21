package com.fundingflex.dto;

import lombok.Data;

@Data
public class FundingUpdateRequestDTO {
    private Long fundingJoinId;
    private int fundingAmount;
    private char nameUndisclosed;
    private char amountUndisclosed;
}
