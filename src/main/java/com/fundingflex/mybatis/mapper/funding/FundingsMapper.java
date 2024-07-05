package com.fundingflex.mybatis.mapper.funding;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.funding.domain.dto.FundingsInfoDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.entity.Images;
import com.fundingflex.funding.domain.form.FundingsForm;


@Mapper
public interface FundingsMapper {

	// 펀딩 정보 저장
    void insertFundings(Fundings newFundings);

    // 펀딩 수정 폼 조회
    FundingsForm selectFundingsForm(@Param("categoryId") Long categoryId,
        @Param("fundingsId") Long fundingsId);

    // 펀딩 정보 조회
    FundingsInfoDTO selectFundingInfo(@Param("categoryId") Long categoryId,
        @Param("fundingsId") Long fundingsId, @Param("userId") Long userId);

    // 펀딩 아이디 확인
    int existsById(Long fundingId);

    // 펀딩 폼 수정
    int updateFunding(@Param("fundingsForm") FundingsForm fundingsForm,
        @Param("categoryId") Long categoryId, @Param("fundingId") Long fundingId);

    
    
    // 이미지 조회
    List<Images> findImagesByFundingsIdOrderBySeqAsc(Long fundingsId);
    
    
    
    // 펀딩 전체 조회
    List<FundingsDTO> getAllFundings(@Param("sortBy") String sortBy, @Param("userId") Long userId);
    List<FundingsDTO> getAllFundings(@Param("sortBy") String sortBy);
      
    // 진행중인 펀딩 조회
    List<FundingsDTO> getInProgressFundings(@Param("sortBy") String sortBy, @Param("userId") Long userId);
    
    
    
    
    // 비회원 펀딩 조회
    List<FundingsDTO> getAllFundingsForGuests(@Param("sortBy") String sortBy);

    // 비로그인 사용자를 위한 진행 중인 펀딩 목록 조회
    List<FundingsDTO> getInProgressFundingsForGuests(@Param("sortBy") String sortBy);
        
    



    // 펀딩 상태 저장
    void insertFundingConditions(FundingConditions fundingConditions);


    
    
    
    // (회원) 진행 중인 펀딩 목록 조회 (카테고리별)
    List<FundingsDTO> getInProgressFundingsByCategory(@Param("categoryId") Long categoryId);
    List<FundingsDTO> getInProgressFundingsByCategory(Map<String, Object> params);

    // (비회원) 진행 중인 펀딩 목록 조회 (카테고리별)
    List<FundingsDTO> getInProgressFundingsByCategoryForGuests(Map<String, Object> params);
   

    
    // 카테고리별 목록
    List<FundingsDTO> getFundingsByCategory(@Param("categoryId") Long categoryId
        , @Param("sortBy") String sortBy);
    List<FundingsDTO> getFundingsByCategory(@Param("categoryId") Long categoryId
            , @Param("sortBy") String sortBy, Long userId);


    // 로그인 사용자를 위한 카테고리별 펀딩 목록 조회
    List<FundingsDTO> getFundingsByCategory(Map<String, Object> params);

    // 비로그인 사용자를 위한 카테고리별 펀딩 목록 조회
    List<FundingsDTO> getFundingsByCategoryForGuests(Map<String, Object> params);
 


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
    
    
    // 펀딩 StatusFlag 업데이트 
	void updateFundinsStatusFlag(@Param("fundings") Fundings fundings,
        @Param("nickName") String nickName);

	
	
	
	
	

    // 사용자가 이미 좋아요를 눌렀는지 확인
    Optional<Fundings> findById(@Param("fundingsId") Long fundingsId);

    // 좋아요 수 업데이트
    int updateLikeCount(@Param("fundingsId") Long fundingsId, @Param("likeCount") int likeCount);
    
    
    // 좋아요 존재 여부 확인
    int existsLike(@Param("fundingsId") Long fundingsId, @Param("userId") Long userId);

    // 좋아요 추가
    void insertLike(@Param("fundingsId") Long fundingsId, @Param("userId") Long userId);

    // 좋아요 삭제
    void deleteLike(@Param("fundingsId") Long fundingsId, @Param("userId") Long userId);

    // 좋아요 수 증가
    void incrementLikeCount(@Param("fundingsId") Long fundingsId);

    // 좋아요 수 감소
    void decrementLikeCount(@Param("fundingsId") Long fundingsId);

    // 펀딩 참여 여부 조회 (있으면 userId 반환)
    Long existsJoinByFundingsId(@Param("fundingsId") Long fundingId,
        @Param("userId") Long userId);
}
