package com.example.demo.travel.controller;

import com.example.demo.travel.controller.dto.TravelOptionDto;
import com.example.demo.travel.service.TravelOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travel/option")
@RequiredArgsConstructor
public class TravelOptionController {
    final TravelOptionService travelOptionService;
    @GetMapping(value = "/list", params = {"country", "city"})
    public ResponseEntity<List<TravelOptionDto>> getOptions(@RequestParam String country, String city) {
        return travelOptionService.getOptions(country, city);
    }
}
