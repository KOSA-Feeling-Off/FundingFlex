package com.fundingflex.pay.domain.dto;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PayDTO {
    private Long paymentId;
    private Long fundingJoinId;
    private Long userId;
    private Long payAmount;
    private String payUuid;
    private Date payTime;
    private String isDeleted = "N";
}
