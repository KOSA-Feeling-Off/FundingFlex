package com.fundingflex.pay.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private String merchantUid;
    private int amount;
    private String buyerName;
}
