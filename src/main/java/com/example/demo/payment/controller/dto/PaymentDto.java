package com.example.demo.payment.controller.dto;

import com.example.demo.moim.controller.form.dto.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentDto {
    private Long id;
    private Integer numInstallments;
    private ParticipantDto participant;
    private String pgProvider;
    private String payMethod;
    private List<InstallmentDto> installments;
    private String customerUid;
}
