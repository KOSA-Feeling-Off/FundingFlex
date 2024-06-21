package com.fundingflex.dto;
import java.util.List;

import com.fundingflex.funding.domain.dto.enums.FundingsStatusEnum;

import lombok.Data;

@Data
public class FundingsDTO {
    private Long fundingsId;
    private String title;
    private String content;
    private FundingsStatusEnum statusFlag; // 추가된 부분
    private int likeCount;
    private int goalAmount;
    private int currentAmount;
    private List<String> imageUrls;
}
