package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MoimDestinationDto {
    private Long id;
    private String country;
    private String city;
}
