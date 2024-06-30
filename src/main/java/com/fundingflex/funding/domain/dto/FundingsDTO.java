package com.fundingflex.funding.domain.dto;
import java.util.List;

import com.fundingflex.funding.domain.enums.FundingsStatusEnum;

import lombok.Data;


@Data
public class FundingsDTO {
    private Long fundingsId;
    private String title;
    private String content;
    private FundingsStatusEnum statusFlag; 
    private int likeCount;
    private int goalAmount;
    private int currentAmount;
    private List<String> imageUrls;
    
    private Long categoryId;
    
    public List<String> getImageUrls() {
        return this.imageUrls;
    }
}
