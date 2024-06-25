package com.fundingflex.funding.domain.dto.dto;

import com.fundingflex.funding.domain.entity.Images;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseFundingInfoDto {

    private FundingsInfoDto fundingsInfoDto;
    private List<Images> imagesList;
}
