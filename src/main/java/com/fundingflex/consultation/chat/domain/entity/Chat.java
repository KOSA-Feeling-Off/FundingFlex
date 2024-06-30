package com.fundingflex.consultation.chat.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHAT")
public class Chat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
	
	@Column(nullable = false)
	private Long chatNumber;
    
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String content;
    
    @Column(nullable = false)
    private Date createdAt;
    
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isAdmin;
}
