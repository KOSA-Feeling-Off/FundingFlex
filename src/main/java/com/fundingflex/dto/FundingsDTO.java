package com.fundingflex.dto;
import java.util.List;

import lombok.Data;

@Data
public class FundingsDTO {
    private Long fundingsId;
    private String title;
    private String content;
    private int likeCount;
    private int goalAmount;
    private int currentAmount;
    private List<String> imageUrls;
}
