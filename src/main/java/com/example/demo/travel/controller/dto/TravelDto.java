package com.example.demo.travel.controller.dto;

import com.example.demo.travel.entity.Airport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TravelDto {
    private Long id;
    private String country;
    private String city;
    private Airport depatureAirport;

    private List<TravelOptionDto> additionalOptions;
}
