package com.fundingflex.funding.domain.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingsForm {

	@NotNull(message = "카테고리를 선택하세요")
	private Long categoryId;		// 카테고리 아이디

	private Long fundingsId;
	
	@NotBlank
	@NotEmpty(message = "제목을 작성해주세요")
	@Size(min = 3, max = 50)
	private String title;			// 제목
	
	@NotBlank
	@NotEmpty(message = "내용을 작성해주세요")
	@Size(min = 3, max = 1000)
	private String content;			// 내용

	@NotNull(message = "펀딩 목표 금액을 작성해주세요")
	@Min(value = 5000, message = "최소 5000원입니다.")
	private int goalAmount;			// 펀딩 목표 금액
}
