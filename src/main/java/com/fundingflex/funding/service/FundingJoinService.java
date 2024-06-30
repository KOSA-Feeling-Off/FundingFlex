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

	public void deleteFunding(Long fundingJoinId) {
		FundingJoin fundingJoin = getFundingJoinById(fundingJoinId);
		fundingsMapper.deleteFundingJoin(fundingJoinId);
		updateFundingConditionsAfterDeletion(fundingJoin.getFundingsId(), fundingJoin.getFundingAmount());
	}

	public Optional<FundingConditions> getFundingConditions(Long fundingsId) {
		return Optional.ofNullable(fundingsMapper.findFundingConditionsByFundingsId(fundingsId));
	}

	private FundingJoin getFundingJoinById(Long fundingJoinId) {
		return fundingsMapper.findFundingJoinById(fundingJoinId)
				.orElseThrow(() -> new RuntimeException("Funding join not found"));
	}

	private void validateFundingAmount(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Funding amount must be greater than zero");
		}
		if (amount % 5000 != 0) {
			throw new IllegalArgumentException("Funding amount must be in multiples of 5000");
		}
	}

	private FundingConditions getOrCreateFundingConditions(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		if (fundingConditions == null) {
			fundingConditions = FundingConditions.of(fundingsId, 0, 0, getGoalAmount(fundingsId));
			fundingsMapper.insertFundingConditions(fundingConditions);
		}
		return fundingConditions;
	}

	private void updateFundingConditions(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		int newCollectedAmount = fundingsMapper.findFundingJoinsByFundingsId(fundingsId).stream()
				.mapToInt(FundingJoin::getFundingAmount).sum();
		fundingConditions.setCollectedAmount(newCollectedAmount);
		fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
		fundingsMapper.updateFundingConditions(fundingConditions);
	}

	private void updateFundingConditionsAfterDeletion(Long fundingsId, int deletedAmount) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		int newCollectedAmount = fundingConditions.getCollectedAmount() - deletedAmount;
		fundingConditions.setCollectedAmount(newCollectedAmount);
		fundingConditions.setPercent(calculatePercent(newCollectedAmount, fundingConditions.getGoalAmount()));
		fundingsMapper.updateFundingConditions(fundingConditions);
	}

	private int getGoalAmount(Long fundingsId) {
		FundingConditions fundingConditions = fundingsMapper.findFundingConditionsByFundingsId(fundingsId);
		return fundingConditions.getGoalAmount();
	}

	private int calculatePercent(int collectedAmount, int goalAmount) {
		return (int) ((collectedAmount / (double) goalAmount) * 100);
	}
}
