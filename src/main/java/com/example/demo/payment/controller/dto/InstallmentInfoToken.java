package com.example.demo.payment.controller.dto;

import lombok.Getter;

@Getter
public class InstallmentInfoToken {
    private Long amount;
    private String merchant_uid;
    private String receipt_url;
}
