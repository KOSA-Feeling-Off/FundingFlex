package com.fundingflex.mypage.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.mybatis.mapper.mypage.MyPageMapper;
import com.fundingflex.mypage.domain.dto.MyPageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
	
	private final MyPageMapper myPageMapper;

	public MyPageDTO findMemberInfoByUserId(Long userId) {
        return myPageMapper.findMemberInfoByUserId(userId);
	}
	
	public List<FundingsDTO> findParticipatedFundingsByUserId(Long userId) {
        return myPageMapper.findParticipatedFundingsByUserId(userId);
    }

	public List<FundingsDTO> findCreatedFundingsByUserId(Long userId) {
        return myPageMapper.findCreatedFundingsByUserId(userId);
    }

    public List<FundingsDTO> findLikedFundingsByUserId(Long userId) {
        return myPageMapper.findLikedFundingsByUserId(userId);
    }
    
    public int countParticipatedFundingsByUserId(Long userId) {
        return myPageMapper.countParticipatedFundingsByUserId(userId);
    }

    public int countCreatedFundingsByUserId(Long userId) {
        return myPageMapper.countCreatedFundingsByUserId(userId);
    }

    public int countLikedFundingsByUserId(Long userId) {
        return myPageMapper.countLikedFundingsByUserId(userId);
    }
    
}
