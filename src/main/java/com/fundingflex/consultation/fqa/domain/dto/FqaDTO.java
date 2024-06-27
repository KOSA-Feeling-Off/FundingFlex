package com.fundingflex.consultation.fqa.domain.dto;

import java.util.Date;
import java.util.List;

import com.fundingflex.consultation.qa.domain.dto.QaImagesDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FQA")
public class FqaDTO {

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