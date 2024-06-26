package com.fundingflex.consultation.fqa.domain.dto;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FQA")
public class FqaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fqaId; // PK 필드

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String title;

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String content;

    @Column(columnDefinition = "NVARCHAR2(255) DEFAULT ' '")
    private String reply;

    @Column(nullable = false)
    private Date createdAt;
    
    @Column(columnDefinition = "NUMBER DEFAULT NULL")
    private Long adminUserId;

    // Getters and setters...

    public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(Long adminUserId) {
		this.adminUserId = adminUserId;
	}

	public Long getFqaId() {
        return fqaId;
    }

    public void setFqaId(Long fqaId) {
        this.fqaId = fqaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}