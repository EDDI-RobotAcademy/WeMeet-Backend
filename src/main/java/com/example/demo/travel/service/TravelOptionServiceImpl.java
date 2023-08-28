package com.example.demo.travel.service;

import com.example.demo.travel.entity.TravelOption;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelOptionServiceImpl implements TravelOptionService{
    @Override
    public ResponseEntity<List<TravelOption>> getOptions(String country, String city) {
        List<TravelOption> responseList = travelOptionRepository.findOptionsByCountryAndCity(country, city);
        return ResponseEntity.ok()
                .body(responseList);
    }
}
