package com.fundingflex.mypage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fundingflex.funding.domain.dto.FundingsDTO;
import com.fundingflex.mybatis.mapper.member.MyPageMapper;
import com.fundingflex.mypage.domain.dto.MyPageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
	
	private final MyPageMapper myPageMapper;

	public MyPageDTO getMyPage(Long userId) {
        return myPageMapper.selectMyPage(userId);
	}
	
	public List<FundingsDTO> getLikedFundings(Long userId) {
        return myPageMapper.findLikedFundings(userId);
    }

    public List<FundingsDTO> getMyFundings(Long userId) {
        return myPageMapper.findMyFundings(userId);
    }

    public List<FundingsDTO> getJoinedFundings(Long userId) {
        return myPageMapper.findJoinedFundings(userId);
    }


    
}
