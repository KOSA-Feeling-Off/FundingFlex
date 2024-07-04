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
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "CONSULTATIONS") // 테이블 이름 지정
public class Consultations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counsul_id")
    private Long counsulId; // PK 필드

    @Column(name = "title", columnDefinition = "NVARCHAR2(255)")
    private String title;

    @Column(name = "content", columnDefinition = "NVARCHAR2(255)")
    private String content;

    @Column(name = "reply", columnDefinition = "NVARCHAR2(255) DEFAULT ' '")
    private String reply;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "is_reply", columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isReply;

    @Column(name = "members_user_id", nullable = false)
    private Long membersUserId;

    @Column(name = "admin_user_id", columnDefinition = "NUMBER(19, 0)")
    @ColumnDefault("NULL")
    private Long adminUserId;

    @OneToMany(mappedBy = "consultations", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Qaimages> images;

}