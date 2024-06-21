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
	private Long fundingJoinId;

	@Column
	private Long fundingsId;

	@Column
	private Long userId;

	@Column
	private int fundingAmount;

	@Column
	private char nameUndisclosed;

	@Column
	private char amountUndisclosed;

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