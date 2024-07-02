package com.fundingflex.mybatis.mapper.funding;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.domain.form.FundingsForm;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface FundingsMapper {

	// 펀딩 정보 저장
    void insertFundings(Fundings newFundings);

    // 펀딩 수정 폼 조회
    FundingsForm selectFundingsForm(@Param("categoryId") Long categoryId,
        @Param("fundingsId") Long fundingsId);

    // 펀딩 정보 조회
    FundingsInfoDTO selectFundingInfo(@Param("categoryId") Long categoryId,
        @Param("fundingsId") Long fundingsId);

    // 펀딩 아이디 확인
    int existsById(Long fundingId);

    // 펀딩 폼 수정
    int updateFunding(@Param("fundingsForm") FundingsForm fundingsForm,
        @Param("categoryId") Long categoryId, @Param("fundingId") Long fundingId);



    // 펀딩 조회 (fundingsId 기준 전체 컬럼 조회)
    Optional<Fundings> findById(@Param("fundingsId") Long fundingsId);

    List<FundingsDTO> getAllFundings(@Param("sortBy") String sortBy);
    
    List<Images> findImagesByFundingsIdOrderBySeqAsc(Long fundingsId);

    
    // 카테고리별 목록
    List<FundingsDTO> getFundingsByCategory(@Param("categoryId") Long categoryId
        , @Param("sortBy") String sortBy);

    // 진행중이 펀딩 조회
    List<FundingsDTO> getInProgressFundings(@Param("sortBy") String sortBy);



    int insertFunding(Fundings fundings);
    int insertImages(List<Images> images);



    // 펀딩 참여 등록
    int insertFundingJoin(FundingJoin fundingJoin);
    
    // 펀딩 참여 수정
    int updateFundingJoin(FundingJoin fundingJoin);

    // 펀딩 참여 취소
    int deleteFundingJoin(Long fundingJoinId);
    
    // 펀딩 참여 조회 (fundingJoinId 기준)
    Optional<FundingJoin> findFundingJoinById(@Param("fundingJoinId") Long fundingJoinId);

    // 펀딩 참여 조회 (fundingsId 기준)
    List<FundingJoin> findFundingJoinsByFundingsId(@Param("fundingsId") Long fundingsId);



    // 펀딩 자금조달 테이블 저장
    void insertFundingConditions(@Param("fundingsId") Long fundingsId,
        @Param("goalAmount") int goalAmount);

    // 펀딩 자금조달 테이블 조회 (fundingsId 기준)
    FundingConditions findFundingConditionsByFundingsId(Long fundingsId);

    // 펀딩 자금조달 테이블 목표금액 수정
    void updateFundingConditionsAmount(@Param("fundingsId") Long fundingsId,
        @Param("goalAmount") int goalAmount);
    
    // 펀딩 자금조달 테이블 수정
    int updateFundingConditions(FundingConditions fundingConditions);
    


    // 위시리스트 테이블 수정
    int updateLikeCount(@Param("fundingsId") Long fundingsId, @Param("likeCount") int likeCount);

    
    // 펀딩 StatusFlag 업데이트 
	void updateFundinsStatusFlag(Fundings fundings);


}
