package com.fundingflex.qa;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONSULTATIONS") // 테이블 이름 지정
public class QaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counsulId; // PK 필드
	
	String title;
	String content;
	String reply;
	Date createdAt;
	
	String isDeleted;
	Long membersUserId;
	Long adminUserId;
	
    public Long getId() {
        return counsulId;
    }

    public void setId(Long counsulId) {
        this.counsulId = counsulId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getMembersUserId() {
        return membersUserId;
    }

    public void setMembersUserId(Long membersUserId) {
        this.membersUserId = membersUserId;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }
    
}
