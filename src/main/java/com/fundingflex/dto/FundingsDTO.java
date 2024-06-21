package com.fundingflex.dto;
import java.util.List;

import com.fundingflex.entity.FundingsStatus;

import lombok.Data;

@Data
public class FundingsDTO {
    private Long fundingsId;
    private String title;
    private String content;
    private FundingsStatus statusFlag; // 추가된 부분
    private int likeCount;
    private int goalAmount;
    private int currentAmount;
    private List<String> imageUrls;
}
