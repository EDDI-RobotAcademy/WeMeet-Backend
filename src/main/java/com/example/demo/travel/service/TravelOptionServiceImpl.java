package com.example.demo.travel.service;

import com.example.demo.travel.controller.dto.TravelOptionDto;
import com.example.demo.travel.entity.TravelOption;
import com.example.demo.travel.repository.TravelOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelOptionServiceImpl implements TravelOptionService{
    final TravelOptionRepository travelOptionRepository;
    @Override
    public ResponseEntity<List<TravelOptionDto>> getOptions(String country, String city) {
        List<TravelOption> travelOptionList = travelOptionRepository.findOptionsByCountryAndCity(country, city);
        List<TravelOptionDto> responseList = travelOptionList.stream().map((to)->TravelOptionDto.builder()
                .id(to.getId())
                .optionName(to.getOptionName())
                .optionPrice(to.getOptionPrice())
                .isDeprecated(to.getIsDeprecated())
                .build()
        ).toList();
        return ResponseEntity.ok(responseList);
    }
}
