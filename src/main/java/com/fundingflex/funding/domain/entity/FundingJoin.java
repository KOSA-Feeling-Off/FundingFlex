package com.fundingflex.funding.domain.entity;

import java.time.LocalDateTime;

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
public class FundingJoin {


	private Long fundingJoinId;	    	// 펀딩 참여 아이디
	private Long fundingsId;			// 펀딩 아이디
	private Long userId;				// 유저 아이디
	private int fundingAmount;			// 펀딩 금액
	private char nameUndisclosed;		// 이름 비공개
	private char amountUndisclosed;		// 금액 비공개
	private LocalDateTime createdAt;	// 생성일
	private String createdBy;			// 생성자
	private LocalDateTime updatedAt;	// 수정일
	private String updatedBy;			// 수정자
	private char isDeleted;				// 삭제 여부
	
	private int goalAmount;
	
	public static FundingJoin of(Long fundingsId, Long userId, int fundingAmount, char nameUndisclosed, char amountUndisclosed, String createdBy) {
        return FundingJoin.builder()
                .fundingsId(fundingsId)
                .userId(userId)
                .fundingAmount(fundingAmount)
                .nameUndisclosed(nameUndisclosed)
                .amountUndisclosed(amountUndisclosed)
                .createdAt(LocalDateTime.now())
                .createdBy(createdBy)
                .isDeleted('N')
                .build();
    }

}

