package com.fundingflex.mybatis.mapper.funding;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;

@Mapper
public interface FundingsMapper {
	
	Optional<Fundings> findById(@Param("fundingsId") Long fundingsId);
	Optional<FundingJoin> findFundingJoinById(@Param("fundingJoinId") Long fundingJoinId);
	
	List<FundingsDTO> getAllFundings(@Param("sortBy") String sortBy);
    List<FundingJoin> findFundingJoinsByFundingsId(@Param("fundingsId") Long fundingsId);
    List<Images> findImagesByFundingsIdOrderBySeqAsc(Long fundingsId);
    
    // 카테고리별 목록
    List<FundingsDTO> getFundingsByCategory(@Param("categoryId") Long categoryId, @Param("sortBy") String sortBy);
    
    // 진행중이 펀딩 조회
    List<FundingsDTO> getInProgressFundings(@Param("sortBy") String sortBy);
    
    int insertFunding(Fundings fundings);
    int insertImages(List<Images> images);
    

    FundingConditions findFundingConditionsByFundingsId(Long fundingsId);
    
    int updateFundingConditions(FundingConditions fundingConditions);
    int insertFundingJoin(FundingJoin fundingJoin);
    int updateFundingJoin(FundingJoin fundingJoin);
    int deleteFundingJoin(Long fundingJoinId);
    int updateLikeCount(@Param("fundingsId") Long fundingsId, @Param("likeCount") int likeCount);

    // 펀딩 정보 저장
    void insertFundings(Fundings newFundings);

    // 펀딩 정보 조회
    FundingsInfoDTO selectFundingInfo(@Param("categoryId") Long categoryId, @Param("fundingsId") Long fundingsId);

    // 펀딩 아이디 확인
    int existsById(Long fundingId);
    
    // 펀딩 상태 저장
    void insertFundingConditions(FundingConditions fundingConditions);
}
