package com.fundingflex.consultation.qa.domain.entity;

import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONSULTATIONS") // 테이블 이름 지정
public class Consultations {

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

	@OneToMany(mappedBy = "consultations", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Qaimages> images;

}
