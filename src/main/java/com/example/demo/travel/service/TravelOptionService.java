package com.example.demo.travel.service;

import com.example.demo.travel.entity.TravelOption;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TravelOptionService {
    ResponseEntity<List<TravelOption>> getOptions(String country, String city);

}
