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


/*
package com.fundingflex.funding.domain.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fundingflex.common.enums.DeleteFlagEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imgId;						// 이미지 아이디
	
	@Column(length = 500)
	private String imageUrl;				// 이미지 url
	
	private int seq;						// 이미지 순서
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundins_id")
    private Fundings fundings;				// 펀딩
	
	
	@CreatedDate
	private LocalDateTime createdAt;		// 생성일시


	@Enumerated(EnumType.STRING)
	@Column(name = "is_deleted", columnDefinition = "CHAR(1)")
	private DeleteFlagEnum isDeleted;		// 삭제여부
}
*/