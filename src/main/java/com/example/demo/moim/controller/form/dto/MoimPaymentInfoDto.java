package com.example.demo.moim.controller.form.dto;

import com.example.demo.payment.controller.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MoimPaymentInfoDto {
    private Long id;
    private Long amountInstallment;
    private Long totalPrice;
    private List<PaymentDto> payments;
    private Integer numInstallments;
}
