package com.example.demo.travel.service;

import com.example.demo.travel.controller.form.TravelReqForm;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TravelService {
    ResponseEntity<Map<String, Object>> createTravel(TravelReqForm reqForm);

    ResponseEntity<List<String>> getCountries();
}