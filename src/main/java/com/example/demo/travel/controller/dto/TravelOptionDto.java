package com.example.demo.travel.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TravelOptionDto {
    private Long id;
    private String optionName;
    private Integer optionPrice;
    private Boolean isDeprecated;
}
