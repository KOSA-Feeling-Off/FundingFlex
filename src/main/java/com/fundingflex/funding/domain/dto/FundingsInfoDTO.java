package com.fundingflex.funding.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FundingsInfoDTO {

    private Long userId;             // 유저 아이디
    private String nickName;         // 유저 닉네임
    private Long categoryId;         // 카테고리 아이디
    private String categoryName;     // 카테고리 명
    private Long fundingsId;         // 펀딩 아이디
    private String title;            // 펀딩 제목
    private String content;          // 펀딩 내용
    private char likesFlag;          // 좋아요 유무
    private int goalAmount;          // 펀딩 목표 금액
    private int statusFlag;          // 펀딩 진행 상태
    private int collectedAmount;     // 펀딩 모금액
    private int percent;             // 퍼센트
}