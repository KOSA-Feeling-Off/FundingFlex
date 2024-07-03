package com.fundingflex.likes.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.fundingflex.funding.domain.entity.Fundings;
import com.fundingflex.mybatis.mapper.category.CategoriesMapper;
import com.fundingflex.mybatis.mapper.funding.FundingsMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

	private final FundingsMapper fundingsMapper;

	private final CategoriesMapper categoriesMapper;

	// 사용자별 좋아요 상태를 저장하기 위한 Set (실제 서비스에서는 데이터베이스를 사용)
	private final Set<String> userLikes = new HashSet<>();
	
	
	// 좋아요 기능
    public boolean likeFunding(Long fundingsId) {
        // 예시 사용자 ID (실제 구현에서는 세션이나 인증 정보를 통해 사용자 ID를 가져와야 함)
        String userId = "exampleUserId";
        String userFundingsKey = userId + "-" + fundingsId;

        if (userLikes.contains(userFundingsKey)) {
            return false; // 이미 좋아요를 누른 경우
        }

        userLikes.add(userFundingsKey);

        Fundings fundings = fundingsMapper.findById(fundingsId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid fundingsId"));

        fundings.setLikeCount(fundings.getLikeCount() + 1);
        fundingsMapper.updateLikeCount(fundingsId, fundings.getLikeCount());
        return true;
    }
    
    // 좋아요 기능 수정
    @Transactional
    public boolean toggleLikeFunding(Long fundingsId, Long userId) {
        if (fundingsMapper.existsLike(fundingsId, userId) > 0) {
            fundingsMapper.deleteLike(fundingsId, userId);
            fundingsMapper.decrementLikeCount(fundingsId);
            return false; // 좋아요 취소됨
        } else {
            fundingsMapper.insertLike(fundingsId, userId);
            fundingsMapper.incrementLikeCount(fundingsId);
            return true; // 좋아요 추가됨
        }
    }
	
}
