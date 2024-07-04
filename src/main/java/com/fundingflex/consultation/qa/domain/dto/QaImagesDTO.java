package com.fundingflex.consultation.qa.domain.dto;

import java.util.Date;
import java.util.List;

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
public class QaImagesDTO {
	private Long qaImageId;
	private String qaImageUrl;
	private int seq;						// 이미지 순서
	private Date createdAt;		// 생성일지
    private QaDTO consultations;
}
