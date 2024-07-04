package com.fundingflex.funding.domain.entity;

import java.time.LocalDateTime;

import com.fundingflex.common.enums.DeleteFlagEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Images {

   private Long imgId;                  // 이미지 아이디
   private Long fundingsId;            // 펀딩 아이디
   private int seq;                  // 이미지 순서
   private String imageUrl;            // 이미지 url
   
   
   private LocalDateTime createdAt;      // 생성일시

   @Enumerated(EnumType.STRING)
   private DeleteFlagEnum isDeleted;      // 삭제여부
   
   
   
   public static Images of(Long fundingsId, int seq, String imageUrl, DeleteFlagEnum isDeleted) {
      return Images.builder()
            .fundingsId(fundingsId)
            .seq(seq)
            .imageUrl(imageUrl)
            .isDeleted(isDeleted)
            .build();
   }
   
}

