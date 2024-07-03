package com.fundingflex.funding.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FundingRequestDTO {
	private Long fundingJoinId; // 수정 및 삭제 시 필요
	private Long fundingsId; 
	private Long userId;
	private int fundingAmount;
	private char nameUndisclosed;
	private char amountUndisclosed;
	
	public void setUserId(String currentUserName) {
		// TODO Auto-generated method stub
		
	}
}
