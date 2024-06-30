package com.fundingflex.consultation.fqa.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FQA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fqaId; // PK 필드

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String title;

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String content;

    @Column(columnDefinition = "NVARCHAR2(255) DEFAULT ' '")
    private String reply;

    @Column(nullable = false)
    private Date createdAt;
    
    @Column(columnDefinition = "NUMBER DEFAULT NULL")
    private Long adminUserId;
}