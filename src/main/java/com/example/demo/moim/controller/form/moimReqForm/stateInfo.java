package com.example.demo.moim.controller.form.moimReqForm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class stateInfo {
    private LocalDateTime startDate;
    private LocalDateTime runwayStartDate;
    private LocalDateTime takeoffStartDate;
    private LocalDateTime departureDate;
    private Integer taxxingPeriod;
    private Integer runwayPeriod;
    private Integer takeoffPeriod;

}
