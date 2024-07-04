package com.fundingflex.consultation.chat.domain.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {

    private Long chatId;
	private Long chatNumber;
    private String content;
    private Date createdAt;
    private Long userId;
    private String isAdmin;
    
    public ChatDTO() {
        if (this.createdAt == null) {
            this.createdAt = new Date();
        }
        if (this.isAdmin == null) {
            this.isAdmin = "N"; // 기본값 설정
        }
    }
    
}
