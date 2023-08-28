package com.example.demo.travel.service;

import com.example.demo.travel.controller.form.TravelReqForm;
import com.example.demo.travel.entity.Travel;
import com.example.demo.travel.entity.TravelOption;
import com.example.demo.travel.repository.TravelOptionRepository;
import com.example.demo.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService{
    final TravelRepository travelRepository;
    final TravelOptionRepository travelOptionRepository;

    @Override
    public ResponseEntity<Map<String, Object>> createTravel(TravelReqForm reqForm) {

        Travel travel = Travel.builder()
                .country(reqForm.getCountry())
                .city(reqForm.getCity())
                .build();
        travel.setTravelOptions(reqForm.getAdditionalOptions().stream().map(TravelOption::new).toList());

        travelRepository.save(travel);
        Map<String, Object> responseMap = Map.of("travel", travel);
        return ResponseEntity.ok()
                .body(responseMap);
    }

    @Override
    public ResponseEntity<List<String>> getCountries() {
        List<String> responseList = travelRepository.findCountries();
        return ResponseEntity.ok()
                .body(responseList);
    }
}
