package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MoimOptionDto {
    private Long id;
    private String optionName;
    private String optionPrice;
}
