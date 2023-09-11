package com.example.demo.moim.controller.form.dto;

import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.MoimOption;
import com.example.demo.travel.entity.Airport;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MoimDestinationDto {
    private Long id;
    private String country;
    private String city;
    private Airport departureAirport;
    private List<MoimOptionDto> options;
}
