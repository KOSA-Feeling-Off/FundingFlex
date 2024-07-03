package com.fundingflex.funding.domain.entity;

import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;
import com.fundingflex.funding.domain.form.FundingsForm;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fundings {

	private Long fundingsId;    					// 펀딩 아이디
    private Long categoryId;    					// 카테고리 아이디
    private Long userId; 							// 유저 아이디
    
    private String categoryName;    				// 카테고리명
    private String title;    						// 제목
    private String content;    						// 내용
    private FundingsStatusEnum statusFlag;			// 펀딩 진행 상태
    private int likeCount;    						// 좋아요 수
    private int goalAmount;    						// 펀딩 목표 금액
    
    private LocalDateTime createdAt;				// 생성 일시
    private String createdBy;						// 생성자
    private LocalDateTime updatedAt;				// 수정 일시
    private String updatedBy;						// 수정자
    
    private DeleteFlagEnum isDeleted;    			// 삭제 여부
    
    
    
    public static Fundings of(FundingsForm fundingsForm, Long userId, String categoryName, String nickName) {
    	return Fundings.builder()
    			.categoryId(fundingsForm.getCategoryId())
    			.userId(userId)
    			.categoryName(categoryName)
    			.title(fundingsForm.getTitle())
    			.content(fundingsForm.getContent())
    			.statusFlag(FundingsStatusEnum.IN_PROGRESS)
    			.goalAmount(fundingsForm.getGoalAmount())
    			.createdBy(nickName)
    			.isDeleted(DeleteFlagEnum.N)
    			.build();
    }
}
