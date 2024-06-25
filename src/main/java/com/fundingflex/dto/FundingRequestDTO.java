package com.fundingflex.dto;

import lombok.Data;

@Data
public class FundingRequestDTO {
	private Long fundingJoinId; // 수정 및 삭제 시 필요
	private Long fundingsId; 
	private Long userId;
	private int fundingAmount;
	private char nameUndisclosed;
	private char amountUndisclosed;
}
