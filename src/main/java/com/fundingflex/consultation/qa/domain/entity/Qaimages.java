package com.fundingflex.consultation.qa.domain.entity;

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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "QAIMAGES") // 테이블 이름 지정
public class Qaimages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long qaImageId;
	
	@Column(length = 500)
	private String qaImageUrl;
	
	private int seq;						// 이미지 순서
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsul_id")
    private Consultations consultations;
	
	@CreatedDate
	private Date createdAt;		// 생성일지
}
