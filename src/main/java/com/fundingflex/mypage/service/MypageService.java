package com.fundingflex.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.mybatis.mapper.mypage.MypageMapper;
import com.fundingflex.mypage.domain.dto.MyPageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
	
	private final MypageMapper myPageMapper;

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
}
