package com.fundingflex.pay.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PayCancelDTO {
	Long fundingJoinsId;
	String paymentId;
	Long amount; //취소 금액
	String reason; //취소 사유
}
