package com.fundingflex.mypage.service;

import org.springframework.stereotype.Service;

import com.fundingflex.mybatis.mapper.mypage.MypageMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MypageMapper mypageMapper;

	/*
	 * public Members findByEmail(String email) { return
	 * mypageMapper.findByEmail(email); }
	 * 
	 * public List<FundingsDTO> findParticipatedFundingsByUserId(Long userId) {
	 * return mypageMapper.findParticipatedFundingsByUserId(userId); }
	 * 
	 * public List<FundingsDTO> findCreatedFundingsByUserId(Long userId) { return
	 * mypageMapper.findCreatedFundingsByUserId(userId); }
	 * 
	 * public List<FundingsDTO> findLikedFundingsByUserId(Long userId) { return
	 * mypageMapper.findLikedFundingsByUserId(userId); }
	 */
}
