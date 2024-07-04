package com.fundingflex.pay.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;
    
    @Column(name = "funding_join_id", nullable = false)
    private Long fundingJoinId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "pay_amount", nullable = false)
    private Long payAmount;
    
    @Column(name = "pay_uuid", nullable = false, length = 20)//유니크 키
    private String payUuid;
    
    @Column(name = "pay_time", nullable = false, updatable = false, insertable = false)
    private Date payTime;
    
    @Column(name = "is_deleted", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isDeleted = "N";
}
