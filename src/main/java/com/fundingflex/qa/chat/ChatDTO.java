package com.fundingflex.qa.chat;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHAT")
public class ChatDTO {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    
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
        	isAdmin = "N";
        }
    }

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
}
