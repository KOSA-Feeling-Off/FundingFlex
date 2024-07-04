package com.fundingflex.pay.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerificationResponse {
    private Amount amount;
    private String status;

    @Getter
    @Setter
    public static class Amount {
        private int total;
        
    }
}