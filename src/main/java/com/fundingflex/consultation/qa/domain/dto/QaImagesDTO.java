package com.fundingflex.consultation.qa.domain.dto;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "QAIMAGES") // 테이블 이름 지정
public class QaImagesDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qaImageId;
	
	@Column(length = 500)
	private String qaImageUrl;
	
	private int seq;						// 이미지 순서
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsul_id")
    private QaDTO consultations;
	
	@CreatedDate
	private Date createdAt;		// 생성일지

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getQaImageId() {
		return qaImageId;
	}

	public void setQaImageId(Long qaImageId) {
		this.qaImageId = qaImageId;
	}

	public String getQaImageUrl() {
		return qaImageUrl;
	}

	public void setQaImageUrl(String qaImageUrl) {
		this.qaImageUrl = qaImageUrl;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public QaDTO getConsultations() {
		return consultations;
	}

	public void setConsultations(QaDTO consultations) {
		this.consultations = consultations;
	}


}
