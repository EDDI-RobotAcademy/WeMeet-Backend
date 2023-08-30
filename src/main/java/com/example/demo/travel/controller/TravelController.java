package com.example.demo.travel.controller;

import com.example.demo.travel.controller.dto.TravelDto;
import com.example.demo.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travel")
@RequiredArgsConstructor
public class TravelController {
    final TravelService travelService;
    @PostMapping
    public ResponseEntity<TravelDto> createTravel(@RequestBody TravelDto reqForm) {
        return travelService.createTravel(reqForm);
    }
    @GetMapping("/country/list")
    public ResponseEntity<List<String>> getCountries() {
        return travelService.getCountries();
    }
    @GetMapping(value = "/city/list", params = {"country"})
    public ResponseEntity<List<String>> getCities(@RequestParam String country) {
        return travelService.getCities(country);
    }


}
