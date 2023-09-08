package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MoimDto {
    private Long id;
    private MoimPaymentInfoDto paymentInfo;
    private MoimDestinationDto moimDestination;
    private MoimParticipantsInfoDto moimParticipantsInfo;
    private MoimContentsDto moimContents;
    private StateDto state;
    private LocalDateTime createdDate;
}
