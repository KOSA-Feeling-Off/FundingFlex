package com.fundingflex.qa;

import java.util.Date;

import com.fundingflex.common.enums.Enums;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "CONSULTATIONS") // 테이블 이름 지정
public class QaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counsulId; // PK 필드

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String title;

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String content;

    @Column(columnDefinition = "NVARCHAR2(255) DEFAULT ' '")
    private String reply;

    @Column(nullable = false)
    private Date createdAt;

    @Column(columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isReply;

    @Column(nullable = false)
    private Long membersUserId;

    @Column(columnDefinition = "NUMBER DEFAULT NULL")
    private Long adminUserId;

    // Getters and setters...

    // 이 메서드는 엔티티가 처음 저장되기 전에 호출됩니다.
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
        if (isReply == null) {
        	isReply = Enums.Delete.N.name();
        }
        if (reply == null) {
            reply = " ";
        }
        // adminUserId는 명시적으로 기본값을 설정하지 않아도 됨
    }

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

	public String getIsReply() {
		return isReply;
	}

	public void setIsReply(String isReply) {
		this.isReply = isReply;
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
