package com.fundingflex.consultation.chat.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_id")
    private Long chatId;
	
	@Column(name = "chat_number", nullable = false)
	private Long chatNumber;
    
    @Column(name = "content",columnDefinition = "NVARCHAR2(255)")
    private String content;
    
    @Column(name = "created_at",nullable = false)
    private Date createdAt;
    
    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "is_admin",nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isAdmin;
}
