package com.example.demo.travel.controller;

import com.example.demo.travel.controller.form.TravelReqForm;
import com.example.demo.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/travel")
@RequiredArgsConstructor
public class TravelController {
    final TravelService travelService;
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTravel(@RequestBody TravelReqForm reqForm) {
        return travelService.createTravel(reqForm);
    }
    @GetMapping("/country/list")
    public ResponseEntity<List<String>> getCountries() {
        return travelService.getCountries();
    }
}
