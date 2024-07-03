package com.fundingflex.funding.service;

import java.util.Optional;

<<<<<<< HEAD
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
=======
import org.springframework.stereotype.Service;
>>>>>>> origin/feat_kmj_01

import com.fundingflex.funding.domain.dto.FundingRequestDTO;
import com.fundingflex.funding.domain.dto.FundingResponseDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;
<<<<<<< HEAD
import com.fundingflex.notification.service.NotificationService;
=======
>>>>>>> origin/feat_kmj_01

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundingJoinService {

   private final FundingsMapper fundingsMapper;
   private final NotificationService notificationService;

<<<<<<< HEAD
=======
	// 펀딩 참여하기
	public FundingResponseDTO joinFunding(FundingRequestDTO requestDto) {
		validateFundingAmount(requestDto.getFundingAmount());
		FundingConditions fundingConditions = getOrCreateFundingConditions(requestDto.getFundingsId());
		int goalAmount = fundingConditions.getGoalAmount();
>>>>>>> origin/feat_kmj_01

   // 펀딩 참여하기
   public FundingResponseDTO joinFunding(FundingRequestDTO requestDtos) {

<<<<<<< HEAD
      // 큐에 작업 추가
      FundingUpdateQueue.addFundingUpdate(requestDtos);
=======
		FundingJoin fundingJoin = FundingJoin.of(requestDto.getFundingsId(), requestDto.getUserId(),
				requestDto.getFundingAmount(), requestDto.getNameUndisclosed(),
				requestDto.getAmountUndisclosed(), "createdByUser" // Replace with actual user info
		);
>>>>>>> origin/feat_kmj_01

      return null;
   }


<<<<<<< HEAD
   @Async
   @Scheduled(fixedDelay = 1000) // 1초마다 실행
   public void processFundingUpdates() throws Exception {
      FundingRequestDTO task = FundingUpdateQueue.takeFundingUpdate();
      while (task != null) {
         // 큐에서 작업을 꺼내와서 처리
         processTask(task);
         task = FundingUpdateQueue.takeFundingUpdate();
      }
   }
   
=======
	// 펀딩 참여 수정
	public FundingResponseDTO updateFunding(FundingRequestDTO requestDto) {
		validateFundingAmount(requestDto.getFundingAmount());
		FundingJoin fundingJoin = getFundingJoinById(requestDto.getFundingJoinId());
>>>>>>> origin/feat_kmj_01

   @Transactional
   public void processTask(FundingRequestDTO task) throws Exception {

      // 펀딩 참여 금액 확인
      validateFundingAmount(task.getFundingAmount());

      // 펀딩
      Long fundingId = task.getFundingsId();

      // 펀딩 정보 조회
      Fundings fundings = fundingsMapper.findById(fundingId)
          .orElseThrow(() -> new NotFoundException("펀딩 정보가 없습니다."));
      
      // 펀딩 참여 금액
      int amount = task.getFundingAmount();

<<<<<<< HEAD
      // 펀딩 자금조달 조회
      FundingConditions fundingConditions =
          fundingsMapper.findFundingConditionsByFundingsId(fundingId);

      /* 새로운 자금 계산 */
      // 모은금액 계산
      int newCollectedAmount = fundingConditions.getCollectedAmount() + amount;
      // 퍼센트 계산
      int newPercentage =
    		  calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount());
      // 100% 여부
      char hundredPercentFlag = fundingConditions.getHundredPercentFlag();

      
      // 100% 도달 시 -> 퍼센트, 플래그 업데이트, 알림 테이블 INSERT
      if (newPercentage >= 100 && hundredPercentFlag == 'N') {

         // 펀딩 자금조달 테이블 UPDATE
         fundingConditions.setCollectedAmount(newCollectedAmount);
         fundingConditions.setPercent(newPercentage);
         fundingConditions.setHundredPercentFlag('Y');
         fundingsMapper.updateFundingConditions(fundingConditions);

         // 펀딩 status '완료'로 업데이트
         fundings.setStatusFlag(FundingsStatusEnum.COMPLETED);
         fundingsMapper.updateFundinsStatusFlag(fundings);
         
         // 알림 테이블 insert
         notificationService.insertNotification(
        		 task.getFundingsId(), 
        		 task.getUserId(),
        		 "'" + fundings.getTitle() + "'" + "의 펀딩이 완료되었습니다.");
      }

      // 펀딩 참여 저장
      FundingJoin fundingJoin =
    		  FundingJoin.of(fundingId, task.getUserId(), task.getFundingAmount(), task.getNameUndisclosed(), task.getAmountUndisclosed(), 'N');
      fundingsMapper.insertFundingJoin(fundingJoin);
//
//
//      FundingConditions fundingConditions = getOrCreateFundingConditions(
//          requestDto.getFundingsId());
//      int goalAmount = fundingConditions.getGoalAmount();
//
//      FundingJoin fundingJoin = FundingJoin.of(requestDto.getFundingsId(), requestDto.getUserId(),
//          requestDto.getFundingAmount(), requestDto.getNameUndisclosed() == 'Y',
//          requestDto.getAmountUndisclosed() == 'Y', "createdByUser" // 로그인 닉네임으로
//      );
//
//
//      updateFundingConditions(requestDto.getFundingsId());

//      return new FundingResponseDTO(fundingJoin.getFundingJoinId());
   }


   // 펀딩 참여 수정
   public FundingResponseDTO updateFunding(FundingRequestDTO requestDto) {
      validateFundingAmount(requestDto.getFundingAmount());
      FundingJoin fundingJoin = getFundingJoinById(requestDto.getFundingJoinId());

      int oldAmount = fundingJoin.getFundingAmount();
      Long fundingsId = fundingJoin.getFundingsId();
      FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);

      if (fundingConditions.getCollectedAmount() - oldAmount + requestDto.getFundingAmount() > fundingConditions
            .getGoalAmount()) {
         throw new IllegalArgumentException("Funding amount exceeds the goal amount");
      }

      fundingJoin.setFundingAmount(requestDto.getFundingAmount());
      fundingJoin.setNameUndisclosed(requestDto.getNameUndisclosed());
      fundingJoin.setAmountUndisclosed(requestDto.getAmountUndisclosed());
      fundingsMapper.updateFundingJoin(fundingJoin);
      updateFundingConditions(fundingsId);

      return new FundingResponseDTO(fundingJoin.getFundingJoinId());
   }

   // 펀딩 참여 취소
   public void deleteFunding(Long fundingJoinId) {
      FundingJoin fundingJoin = getFundingJoinById(fundingJoinId);
      fundingsMapper.deleteFundingJoin(fundingJoinId);
      updateFundingConditionsAfterDeletion(fundingJoin.getFundingsId(), fundingJoin.getFundingAmount());
   }

   
   // 펀딩 자금조달 조회
   public Optional<FundingConditions> getFundingConditions(Long fundingsId) {
      return Optional.ofNullable(fundingsMapper.findFundingConditionsByFundingsId(fundingsId));
   }

   
   // 펀딩 참여 아이디로 조회
   private FundingJoin getFundingJoinById(Long fundingJoinId) {
      return fundingsMapper.findFundingJoinById(fundingJoinId)
            .orElseThrow(() -> new RuntimeException("Funding join not found"));
   }

   // 펀딩 금액 조건 제한
   private void validateFundingAmount(int amount) {
      if (amount <= 0) {
         throw new IllegalArgumentException("펀딩 금액을 입력하세요");
      }
      if (amount % 5000 != 0) {
         throw new IllegalArgumentException("펀딩 금액은 5,000원 단위로 입력해주세요");
      }
   }

   
   // 펀딩 상태 생성 및 조회
   private FundingConditions getOrCreateFundingConditions(Long fundingsId) {
      return fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
   }

   // 펀딩 자금조달 업데이트
   private void updateFundingConditions(Long fundingsId) {
      FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
      int newCollectedAmount = fundingsMapper.findFundingJoinsByFundingsId(fundingsId).stream()
            .mapToInt(FundingJoin::getFundingAmount).sum();
      fundingConditions.setCollectedAmount(newCollectedAmount);
      fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
      fundingsMapper.updateFundingConditions(fundingConditions);
   }

   // 펀딩 상태 삭제
   private void updateFundingConditionsAfterDeletion(Long fundingsId, int deletedAmount) {
      FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
      int newCollectedAmount = fundingConditions.getCollectedAmount() - deletedAmount;
      fundingConditions.setCollectedAmount(newCollectedAmount);
      fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
      fundingsMapper.updateFundingConditions(fundingConditions);
   }

   // 펀딩 금액 백분율
   private int calculatePercent(int collectedAmount, int goalAmount) {
      return (int) ((collectedAmount / (double) goalAmount) * 100);
   }
=======
	// 펀딩 참여 삭제
	public void deleteFunding(Long fundingJoinId) {
		FundingJoin fundingJoin = getFundingJoinById(fundingJoinId);
		fundingsMapper.deleteFundingJoin(fundingJoinId);
		updateFundingConditionsAfterDeletion(fundingJoin.getFundingsId(), fundingJoin.getFundingAmount());
	}

	// 펀딩 상태 조회
	public Optional<FundingConditions> getFundingConditions(Long fundingsId) {
		return Optional.ofNullable(fundingsMapper.findFundingConditionsByFundingsId(fundingsId));
	}

	// 펀딩 참여 아이디로 조회
	private FundingJoin getFundingJoinById(Long fundingJoinId) {
		return fundingsMapper.findFundingJoinById(fundingJoinId)
				.orElseThrow(() -> new RuntimeException("Funding join not found"));
	}

	// 펀딩 금액 조건 제한
	private void validateFundingAmount(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Funding amount must be greater than zero");
		}
		if (amount % 5000 != 0) {
			throw new IllegalArgumentException("Funding amount must be in multiples of 5000");
		}
	}

	// 펀딩 상태 생성 및 조회
	private FundingConditions getOrCreateFundingConditions(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		if (fundingConditions == null) {
			fundingConditions = FundingConditions.of(fundingsId, 0, 0, getGoalAmount(fundingsId));
			fundingsMapper.insertFundingConditions(fundingConditions);
		}
		return fundingConditions;
	}

	// 펀딩 상태 업데이트
	private void updateFundingConditions(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		int newCollectedAmount = fundingsMapper.findFundingJoinsByFundingsId(fundingsId).stream()
				.mapToInt(FundingJoin::getFundingAmount).sum();
		fundingConditions.setCollectedAmount(newCollectedAmount);
		fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
		fundingsMapper.updateFundingConditions(fundingConditions);
	}

	// 펀딩 상태 삭제
	private void updateFundingConditionsAfterDeletion(Long fundingsId, int deletedAmount) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		int newCollectedAmount = fundingConditions.getCollectedAmount() - deletedAmount;
		fundingConditions.setCollectedAmount(newCollectedAmount);
		fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
		fundingsMapper.updateFundingConditions(fundingConditions);
	}

	// 펀딩 목표 금액
	private int getGoalAmount(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		return fundingConditions.getGoalAmount();
	}

	// 펀딩 금액 백분율
	private int calculatePercent(int collectedAmount, int goalAmount) {
		return (int) ((collectedAmount / (double) goalAmount) * 100);
	}
>>>>>>> origin/feat_kmj_01
}
