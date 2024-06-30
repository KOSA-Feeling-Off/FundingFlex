package com.fundingflex.consultation.fqa.domain.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FqaDTO {
    private Long fqaId;
    private String title;
    private String content;
    private String reply;
    private Date createdAt;
    private Long adminUserId;
}