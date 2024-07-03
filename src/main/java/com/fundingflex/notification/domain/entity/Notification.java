package com.fundingflex.notification.domain.entity;

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
public class Notification {

	private Long notiId;			// 알림 아이디
	private Long fcId;				// 펀딩 자금조달 아이디
	private Long userId;			// 유저 아이디
	private String message;			// 알림 내용
	private String createdAt;		// 생성 일시
	private char isRead;			// 읽음 여부
	private String readAt;			// 읽은 일시
	
	
	
	public static Notification of(Long fundingConditionId, Long userId, String message) {
		return Notification.builder()
				.userId(userId)
				.fcId(fundingConditionId)
				.message(message)
				.build();
	}
}
