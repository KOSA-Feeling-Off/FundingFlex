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

	private Long noti_id;			// 알림 아이디
	private Long user_id;			// 유저 아이디
	private Long fc_id;				// 펀딩 자금조달 아이디
	private String message;			// 알림 내용
	private String created_at;		// 생성 일시
	private char is_read;			// 읽음 여부
	private String read_at;			// 읽은 일시
	
	
	
	public static Notification of(Long fundingsId, Long userId, String message) {
		return Notification.builder()
				.user_id(1L)
				.fc_id(1L)
				.message("")
				.created_at("createdAt")
				.is_read('N')
				.read_at("readAt")
				.build();
	}
}
