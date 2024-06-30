package com.fundingflex.funding.domain.entity;

import com.fundingflex.common.enums.DeleteFlagEnum;
import com.fundingflex.funding.domain.enums.FundingsStatusEnum;
import com.fundingflex.member.domain.dto.MembersDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fundings", indexes = {
	    @Index(name = "idx_category_id", columnList = "category_id")
	})
public class Fundings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fundingsId;	// 펀딩 아이디

	@Column(name = "category_id")
    private Long categoryId; // Categories 테이블의 category_id 열 참조

	@Column(length = 20)
	private String categoryName;		// Categories 테이블의 category_name

	@Column(length = 255)
	private String title;		// 제목

	@Column(length = 1000)
	private String content;		// 내용

	@Column
	@Enumerated(EnumType.ORDINAL)
	private FundingsStatusEnum statusFlag;		// 펀딩 진행 상태

	private int likeCount;		// 좋아요 수

	private int goalAmount;		// 펀딩 목표 금액


	// 이미지
	@OneToMany(mappedBy = "fundings", orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Images> imageList;


	// 유저
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private MembersDTO membersDTO;


	// 펀딩 조건
	@OneToOne(mappedBy = "fundings", cascade = CascadeType.ALL)
	private FundingConditions fundingConditions;


	@CreatedDate
	private LocalDateTime createdAt;

	@CreatedBy
	private String createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private String updatedBy;


	@Column(columnDefinition = "CHAR(1)")
	@Enumerated(EnumType.STRING)
	private DeleteFlagEnum isDeleted;		// 삭제 여부
}
