package com.fundingflex.funding.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funding_joins")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingJoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundingJoinId;	    	// 펀딩 참여 아이디

	@Column
	private Long fundingsId;			// 펀딩 아이디

	@Column
	private Long userId;				// 유저 아이디

	@Column
	private int fundingAmount;			// 펀딩 금액

	@Column
	private char nameUndisclosed;		// 이름 비공개

	@Column
	private char amountUndisclosed;		// 금액 비공개

	@CreatedDate
	private LocalDateTime createdAt;	// 생성일

	@CreatedBy
	private String createdBy;			// 생성자

	@LastModifiedDate
	private LocalDateTime updatedAt;	// 수정일

	@LastModifiedBy
	private String updatedBy;			// 수정자

	private char isDeleted;				// 삭제 여부
}