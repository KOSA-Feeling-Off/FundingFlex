package com.fundingflex.mybatis.mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.mypage.domain.dto.MyPageDTO;

@Mapper
public interface MyPageMapper {

	MyPageDTO selectMyPage(Long userId);
	
	List<FundingsDTO> findLikedFundings(@Param("userId") Long userId);

    List<FundingsDTO> findMyFundings(@Param("userId") Long userId);

    List<FundingsDTO> findJoinedFundings(@Param("userId") Long userId);
}
