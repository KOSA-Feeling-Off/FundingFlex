package com.fundingflex.mybatis.mapper.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.mypage.domain.dto.MyPageDTO;

@Mapper
public interface MyPageMapper {

    MyPageDTO findMemberInfoByUserId(@Param("userId") Long userId);

    List<FundingsDTO> findParticipatedFundingsByUserId(@Param("userId") Long userId);

    List<FundingsDTO> findCreatedFundingsByUserId(@Param("userId") Long userId);

    List<FundingsDTO> findLikedFundingsByUserId(@Param("userId") Long userId);

	int countParticipatedFundingsByUserId(Long userId);

	int countCreatedFundingsByUserId(Long userId);

	int countLikedFundingsByUserId(Long userId);
}
