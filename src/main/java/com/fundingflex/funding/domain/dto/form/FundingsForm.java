package com.fundingflex.funding.domain.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundingsForm {
	
	@NotBlank
	@NotEmpty
	private Long categoryId;		// 카테고리 아이디
	private String categoryName;	// 카테고리 명
	
	@NotBlank
	@NotEmpty
	@Size(min = 3, max = 50)
	private String title;			// 제목
	
	@NotBlank
	@NotEmpty
	@Size(min = 3, max = 1000)
	private String content;			// 내용
	
	@NotBlank
	@NotEmpty
	@Size(min = 1000)
	private int goalAmount;			// 펀딩 목표 금액
}
