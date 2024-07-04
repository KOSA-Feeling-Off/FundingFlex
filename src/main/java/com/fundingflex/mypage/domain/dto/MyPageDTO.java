package com.fundingflex.mypage.domain.dto;

import java.util.List;

import com.fundingflex.funding.domain.dto.FundingsDTO;

import lombok.Data;

@Data
public class MyPageDTO {
    private String nickname;
    private String title;
    private String content;
    private String profileUrl;
    private int goalAmount;
    private Long fundingsId;
    private String imageUrl;
    private int percent;
    private int collectedAmount;
}
