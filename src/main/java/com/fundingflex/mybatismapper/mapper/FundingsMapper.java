package com.fundingflex.mybatismapper.mapper;

import com.fundingflex.funding.domain.dto.dto.FundingsInfoDto;
import com.fundingflex.funding.domain.entity.Fundings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FundingsMapper {

    // 펀딩 정보 조회
    FundingsInfoDto selectFundingInfo(@Param("categoryId") Long categoryId,
        @Param("fundingsId") Long fundingsId);
}
