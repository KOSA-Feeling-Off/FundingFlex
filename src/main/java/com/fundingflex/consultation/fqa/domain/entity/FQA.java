package com.fundingflex.consultation.fqa.domain.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class FQA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fqa_id")
    private Long fqaId; // PK 필드

    @Column(name = "title", columnDefinition = "NVARCHAR2(255)")
    private String title;

    @Column(name = "content", columnDefinition = "NVARCHAR2(255)")
    private String content;

    @Column(name = "reply", columnDefinition = "NVARCHAR2(255) DEFAULT ' '")
    private String reply;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "admin_user_id", columnDefinition = "NUMBER(19, 0)")
    @ColumnDefault("NULL")
    private Long adminUserId;
}
