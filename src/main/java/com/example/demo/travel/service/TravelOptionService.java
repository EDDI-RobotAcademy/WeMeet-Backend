package com.example.demo.travel.service;

import com.example.demo.travel.controller.dto.TravelOptionDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TravelOptionService {
    ResponseEntity<List<TravelOptionDto>> getOptions(String country, String city);

}
