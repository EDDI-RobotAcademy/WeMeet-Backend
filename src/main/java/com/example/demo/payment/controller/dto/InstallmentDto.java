package com.example.demo.payment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InstallmentDto {
    private Long id;
    private PaymentDto payment;
    private Long amount;
    private String merchantUid;
}
