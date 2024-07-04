package com.fundingflex.funding.domain.dto;

import java.util.List;

import com.fundingflex.funding.domain.enums.FundingsStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundingsDTO {
    private Long fundingsId;
    private String title;
    private String content;
    private FundingsStatusEnum statusFlag; 
    private int likeCount;
    private int goalAmount;
    private int currentAmount;
    private List<String> imageUrls;
    private int percent; // 추가: 진행률 퍼센트
    private String nickname;
    
    private Long categoryId;
    
    public List<String> getImageUrls() {
        return this.imageUrls;
    }
    
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    private String existsFlag; 
}
