package com.fundingflex.member.domain.dto;

import java.util.List;

import com.fundingflex.funding.domain.dto.FundingsDTO;

import lombok.Data;

@Data
public class MyPageDTO {
	private String email;
    private String nickname;
    private String profileUrl;
    private List<FundingsDTO> createdFundings;
    private List<FundingsDTO> joinedFundings;
    private List<FundingsDTO> likedFundings;
}
