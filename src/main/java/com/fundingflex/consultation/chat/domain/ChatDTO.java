package com.fundingflex.consultation.chat.domain;

import java.util.Date;
import java.util.List;

import com.fundingflex.common.enums.Enums;
import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CHAT")
public class ChatDTO {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
	
	private Long chatNumber;
    
    @Column(columnDefinition = "NVARCHAR2(255)")
    private String content;
    
    @Column(nullable = false)
    private Date createdAt;
    
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isAdmin;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (isAdmin == null) {
        	isAdmin = Enums.Admin.N.name();
        }
    }
}
