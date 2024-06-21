package com.fundingflex.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fundings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundings_id; // 펀딩 아이디

	@Column(length = 255)
	private String title; // 제목

	@Column(length = 1000)
	private String content; // 내용

	/*
	 * @Column
	 * 
	 * @Enumerated(EnumType.ORDINAL) private FundingsStatus statusFlag; // 펀딩 진행 상태
	 */

	private int likeCount = 0; // 좋아요 수

	private int goalAmount; // 펀딩 목표 금액

	@Transient
	private int currentAmount;

	@OneToMany(mappedBy = "fundings", orphanRemoval = true)
	@JsonManagedReference
	private List<Images> imageList;
	
	@CreatedDate
	private LocalDateTime createdAt;

	@CreatedBy
	private String createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private String updatedBy;

	private char isDeleted;

}