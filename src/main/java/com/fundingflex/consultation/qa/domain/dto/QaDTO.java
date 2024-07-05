package com.fundingflex.consultation.qa.domain.dto;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Getter
@Setter
public class QaDTO {
    private Long counsulId;
    private String title;
    private String content;
    private String reply;
    private Date createdAt;
//    private String reply;
    private Long membersUserId;
    private Long adminUserId;
    private List<QaImagesDTO> images;
}
