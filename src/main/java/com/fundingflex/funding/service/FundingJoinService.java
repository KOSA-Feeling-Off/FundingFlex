package com.fundingflex.funding.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fundingflex.funding.domain.dto.FundingRequestDTO;
import com.fundingflex.funding.domain.dto.FundingResponseDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundingJoinService {

	private final FundingsMapper fundingsMapper;

	// 펀딩 참여하기
	public FundingResponseDTO joinFunding(FundingRequestDTO requestDto) {
		validateFundingAmount(requestDto.getFundingAmount());
		FundingConditions fundingConditions = getOrCreateFundingConditions(requestDto.getFundingsId());
		int goalAmount = fundingConditions.getGoalAmount();

		if (fundingConditions.getCollectedAmount() + requestDto.getFundingAmount() > goalAmount) {
			throw new IllegalArgumentException("Funding amount exceeds the goal amount");
		}

		FundingJoin fundingJoin = FundingJoin.of(requestDto.getFundingsId(), requestDto.getUserId(),
				requestDto.getFundingAmount(), requestDto.getNameUndisclosed() == 'Y',
				requestDto.getAmountUndisclosed() == 'Y', "createdByUser" // Replace with actual user info
		);

		fundingsMapper.insertFundingJoin(fundingJoin);
		updateFundingConditions(requestDto.getFundingsId());

		return new FundingResponseDTO(fundingJoin.getFundingJoinId());
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
}
