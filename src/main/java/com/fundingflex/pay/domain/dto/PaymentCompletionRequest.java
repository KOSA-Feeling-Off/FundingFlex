package com.fundingflex.pay.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCompletionRequest {
    private String paymentId;
    private int amount;
}