package com.example.demo.travel.service;

import com.example.demo.travel.controller.dto.TravelDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TravelService {
    ResponseEntity<TravelDto> createTravel(TravelDto reqForm);

    ResponseEntity<List<String>> getCountries();

    ResponseEntity<List<String>> getCities(String country);

}
