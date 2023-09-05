package com.example.demo.payment.controller.dto;

import com.example.demo.moim.entity.Participant;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentReqForm {
    private Long totalPrice;

    private String applyNum;
    private String customerUid;
    private String merchantUid;
    private Long paidAmount;
    private LocalDateTime paidAt;
    private String payMethod;
    private String pgProvider;
    private String pgTid;
    private String pgType;
    private String receiptUrl;
}
