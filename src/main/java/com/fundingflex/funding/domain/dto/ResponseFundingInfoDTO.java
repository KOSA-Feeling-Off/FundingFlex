package com.fundingflex.funding.domain.dto;

import com.fundingflex.funding.domain.entity.Images;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseFundingInfoDTO {

    private FundingsInfoDTO fundingsInfoDto;
    private List<Images> imagesList;
    private Long fundingJoinUserId;
}