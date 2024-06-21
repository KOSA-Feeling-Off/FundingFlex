package com.fundingflex.funding.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fundingflex.dto.FundingRequestDTO;
import com.fundingflex.dto.FundingResponseDTO;
import com.fundingflex.funding.domain.entity.FundingConditions;
import com.fundingflex.funding.domain.entity.FundingJoin;
import com.fundingflex.funding.repository.FundingConditionsRepository;
import com.fundingflex.funding.repository.FundingJoinRepository;
import com.fundingflex.funding.repository.FundingsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundingJoinService {

    private final FundingJoinRepository fundingJoinRepository;
//    private final UserRepository userRepository;
    private final FundingsRepository fundingsRepository;
    private final FundingConditionsRepository fundingConditionsRepository;

  

    public FundingResponseDTO joinFunding(FundingRequestDTO requestDto) {
        try {
        	
        		// TODO member 로 변경해야함
//            if (!userRepository.existsById(requestDto.getUserId())) {
//                throw new IllegalArgumentException("User ID does not exist");
//            }
//            if (!fundingsRepository.existsById(requestDto.getFundingsId())) {
//                throw new IllegalArgumentException("Fundings ID does not exist");
//            }

            FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(requestDto.getFundingsId());
            if (fundingConditions == null) {
                fundingConditions = new FundingConditions();
                fundingConditions.setFundingsId(requestDto.getFundingsId());
                fundingConditions.setCollectedAmount(0);
            }

            int currentCollectedAmount = fundingConditions.getCollectedAmount();
            int goalAmount = fundingsRepository.findById(requestDto.getFundingsId())
                    .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
            if (currentCollectedAmount + requestDto.getFundingAmount() > goalAmount) {
                throw new IllegalArgumentException("Funding amount exceeds the goal amount");
            }

            FundingJoin fundingJoin = new FundingJoin();
            fundingJoin.setFundingsId(requestDto.getFundingsId());
            fundingJoin.setUserId(requestDto.getUserId());
            fundingJoin.setFundingAmount(requestDto.getFundingAmount());
            fundingJoin.setNameUndisclosed(requestDto.getNameUndisclosed());
            fundingJoin.setAmountUndisclosed(requestDto.getAmountUndisclosed());
            FundingJoin savedFundingJoin = fundingJoinRepository.save(fundingJoin);

            updateFundingConditions(requestDto.getFundingsId());

            return new FundingResponseDTO(savedFundingJoin.getFundingJoinId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("Failed to join funding", e);
        }
    }

    public FundingResponseDTO updateFunding(FundingRequestDTO requestDto) {
        try {
            if (requestDto.getFundingJoinId() == null) {
                throw new IllegalArgumentException("FundingJoin ID must not be null");
            }
            if (requestDto.getFundingAmount() % 5000 != 0) {
                throw new IllegalArgumentException("Funding amount must be in multiples of 5000");
            }

            FundingJoin fundingJoin = fundingJoinRepository.findById(requestDto.getFundingJoinId())
                .orElseThrow(() -> new RuntimeException("펀딩 참여 정보를 찾을 수 없습니다."));

            int oldAmount = fundingJoin.getFundingAmount();
            Long fundingsId = fundingJoin.getFundingsId();
            int currentCollectedAmount = fundingConditionsRepository.findByFundingsId(fundingsId).getCollectedAmount();
            int goalAmount = fundingsRepository.findById(fundingsId)
                    .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
            if (currentCollectedAmount - oldAmount + requestDto.getFundingAmount() > goalAmount) {
                throw new IllegalArgumentException("Funding amount exceeds the goal amount");
            }

            fundingJoin.setFundingAmount(requestDto.getFundingAmount());
            fundingJoin.setNameUndisclosed(requestDto.getNameUndisclosed());
            fundingJoin.setAmountUndisclosed(requestDto.getAmountUndisclosed());
            FundingJoin savedFundingJoin = fundingJoinRepository.save(fundingJoin);

            updateFundingConditions(fundingsId);

            return new FundingResponseDTO(savedFundingJoin.getFundingJoinId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("펀딩 수정에 실패했습니다.", e);
        }
    }

    private void updateFundingConditions(Long fundingsId) {
        FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(fundingsId);
        if (fundingConditions == null) {
            fundingConditions = new FundingConditions();
            fundingConditions.setFundingsId(fundingsId);
            fundingConditions.setCollectedAmount(0);
        }

        int newCollectedAmount = fundingJoinRepository.findAll().stream()
            .filter(join -> join.getFundingsId().equals(fundingsId))
            .mapToInt(FundingJoin::getFundingAmount)
            .sum();
        fundingConditions.setCollectedAmount(newCollectedAmount);

        int goalAmount = fundingsRepository.findById(fundingsId)
                .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
        int percent = (int) ((newCollectedAmount / (double) goalAmount) * 100);
        fundingConditions.setPercent(percent);

        fundingConditionsRepository.save(fundingConditions);
    }

    public void deleteFunding(Long fundingJoinId) {
        try {
            FundingJoin fundingJoin = fundingJoinRepository.findById(fundingJoinId)
                .orElseThrow(() -> new RuntimeException("펀딩 참여 정보를 찾을 수 없습니다."));
            int oldAmount = fundingJoin.getFundingAmount();
            Long fundingsId = fundingJoin.getFundingsId();
            fundingJoinRepository.delete(fundingJoin);
            updateFundingConditions(fundingsId, oldAmount, 0);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("펀딩 삭제에 실패했습니다.", e);
        }
    }

    private void updateFundingConditions(Long fundingsId, int oldAmount, int newAmount) {
        FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(fundingsId);
        if (fundingConditions == null) {
            fundingConditions = new FundingConditions();
            fundingConditions.setFundingsId(fundingsId);
            fundingConditions.setCollectedAmount(0);
        }

        int currentCollectedAmount = fundingConditions.getCollectedAmount();
        int adjustedCollectedAmount = currentCollectedAmount - oldAmount + newAmount;
        fundingConditions.setCollectedAmount(adjustedCollectedAmount);

        int goalAmount = fundingsRepository.findById(fundingsId)
                .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
        int percent = (int) ((adjustedCollectedAmount / (double) goalAmount) * 100);
        fundingConditions.setPercent(percent);

        fundingConditionsRepository.save(fundingConditions);
    }

    public Optional<FundingConditions> getFundingConditions(Long fundingsId) {
        return fundingConditionsRepository.findById(fundingsId);
    }
}







/*
package com.fundingflex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundingflex.dto.FundingRequestDTO;
import com.fundingflex.dto.FundingResponseDTO;
import com.fundingflex.entity.FundingConditions;
import com.fundingflex.entity.FundingJoin;
import com.fundingflex.repository.FundingConditionsRepository;
import com.fundingflex.repository.FundingJoinRepository;
import com.fundingflex.repository.FundingsRepository;
import com.fundingflex.repository.UserRepository;

@Service
public class FundingJoinService {

    private final FundingJoinRepository fundingJoinRepository;
    private final UserRepository userRepository;
    private final FundingsRepository fundingsRepository;
    private final FundingConditionsRepository fundingConditionsRepository;

    @Autowired
    public FundingJoinService(FundingJoinRepository fundingJoinRepository, UserRepository userRepository,
                              FundingsRepository fundingsRepository, FundingConditionsRepository fundingConditionsRepository) {
        this.fundingJoinRepository = fundingJoinRepository;
        this.userRepository = userRepository;
        this.fundingsRepository = fundingsRepository;
        this.fundingConditionsRepository = fundingConditionsRepository;
    }

    public FundingResponseDTO joinFunding(FundingRequestDTO requestDto) {
        try {
            if (!userRepository.existsById(requestDto.getUserId())) {
                throw new IllegalArgumentException("User ID does not exist");
            }
            if (!fundingsRepository.existsById(requestDto.getFundingsId())) {
                throw new IllegalArgumentException("Fundings ID does not exist");
            }

            FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(requestDto.getFundingsId());
            if (fundingConditions == null) {
                fundingConditions = new FundingConditions();
                fundingConditions.setFundingsId(requestDto.getFundingsId());
                fundingConditions.setCollectedAmount(0);
            }

            int currentCollectedAmount = fundingConditions.getCollectedAmount();
            int goalAmount = fundingsRepository.findById(requestDto.getFundingsId())
                    .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
            if (currentCollectedAmount + requestDto.getFundingAmount() > goalAmount) {
                throw new IllegalArgumentException("Funding amount exceeds the goal amount");
            }

            FundingJoin fundingJoin = new FundingJoin();
            fundingJoin.setFundingsId(requestDto.getFundingsId());
            fundingJoin.setUserId(requestDto.getUserId());
            fundingJoin.setFundingAmount(requestDto.getFundingAmount());
            fundingJoin.setNameUndisclosed(requestDto.getNameUndisclosed());
            fundingJoin.setAmountUndisclosed(requestDto.getAmountUndisclosed());
            FundingJoin savedFundingJoin = fundingJoinRepository.save(fundingJoin);

            updateFundingConditions(requestDto.getFundingsId());

            return new FundingResponseDTO(savedFundingJoin.getFundingJoinId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("Failed to join funding", e);
        }
    }

    public FundingResponseDTO updateFunding(FundingRequestDTO requestDto) {
        try {
            if (requestDto.getFundingJoinId() == null) {
                throw new IllegalArgumentException("FundingJoin ID must not be null");
            }
            if (requestDto.getFundingAmount() % 5000 != 0) {
                throw new IllegalArgumentException("Funding amount must be in multiples of 5000");
            }

            FundingJoin fundingJoin = fundingJoinRepository.findById(requestDto.getFundingJoinId())
                .orElseThrow(() -> new RuntimeException("펀딩 참여 정보를 찾을 수 없습니다."));

            int oldAmount = fundingJoin.getFundingAmount();
            Long fundingsId = fundingJoin.getFundingsId();
            int currentCollectedAmount = fundingConditionsRepository.findByFundingsId(fundingsId).getCollectedAmount();
            int goalAmount = fundingsRepository.findById(fundingsId)
                    .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
            if (currentCollectedAmount - oldAmount + requestDto.getFundingAmount() > goalAmount) {
                throw new IllegalArgumentException("Funding amount exceeds the goal amount");
            }

            fundingJoin.setFundingAmount(requestDto.getFundingAmount());
            fundingJoin.setNameUndisclosed(requestDto.getNameUndisclosed());
            fundingJoin.setAmountUndisclosed(requestDto.getAmountUndisclosed());
            FundingJoin savedFundingJoin = fundingJoinRepository.save(fundingJoin);

            updateFundingConditions(fundingsId);

            return new FundingResponseDTO(savedFundingJoin.getFundingJoinId());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("펀딩 수정에 실패했습니다.", e);
        }
    }

    private void updateFundingConditions(Long fundingsId) {
        FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(fundingsId);
        if (fundingConditions == null) {
            fundingConditions = new FundingConditions();
            fundingConditions.setFundingsId(fundingsId);
            fundingConditions.setCollectedAmount(0);
        }

        int newCollectedAmount = fundingJoinRepository.findAll().stream()
            .filter(join -> join.getFundingsId().equals(fundingsId))
            .mapToInt(FundingJoin::getFundingAmount)
            .sum();
        fundingConditions.setCollectedAmount(newCollectedAmount);

        int goalAmount = fundingsRepository.findById(fundingsId)
                .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
        int percent = (int) ((newCollectedAmount / (double) goalAmount) * 100);
        fundingConditions.setPercent(percent);

        fundingConditionsRepository.save(fundingConditions);
    }

    public void deleteFunding(Long fundingJoinId) {
        try {
            FundingJoin fundingJoin = fundingJoinRepository.findById(fundingJoinId)
                .orElseThrow(() -> new RuntimeException("펀딩 참여 정보를 찾을 수 없습니다."));
            int oldAmount = fundingJoin.getFundingAmount();
            Long fundingsId = fundingJoin.getFundingsId();
            fundingJoinRepository.delete(fundingJoin);
            updateFundingConditions(fundingsId, oldAmount, 0);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new RuntimeException("펀딩 삭제에 실패했습니다.", e);
        }
    }

    private void updateFundingConditions(Long fundingsId, int oldAmount, int newAmount) {
        FundingConditions fundingConditions = fundingConditionsRepository.findByFundingsId(fundingsId);
        if (fundingConditions == null) {
            fundingConditions = new FundingConditions();
            fundingConditions.setFundingsId(fundingsId);
            fundingConditions.setCollectedAmount(0);
        }

        int currentCollectedAmount = fundingConditions.getCollectedAmount();
        int adjustedCollectedAmount = currentCollectedAmount - oldAmount + newAmount;
        fundingConditions.setCollectedAmount(adjustedCollectedAmount);

        int goalAmount = fundingsRepository.findById(fundingsId)
                .orElseThrow(() -> new RuntimeException("Fundings not found")).getGoalAmount();
        int percent = (int) ((adjustedCollectedAmount / (double) goalAmount) * 100);
        fundingConditions.setPercent(percent);

        fundingConditionsRepository.save(fundingConditions);
    }
}
*/
