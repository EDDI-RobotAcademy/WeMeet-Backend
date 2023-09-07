package com.example.demo.travel.service;

import com.example.demo.travel.controller.dto.TravelDto;
import com.example.demo.travel.controller.dto.TravelOptionDto;
import com.example.demo.travel.entity.Airport;
import com.example.demo.travel.entity.Travel;
import com.example.demo.travel.entity.TravelOption;
import com.example.demo.travel.repository.TravelOptionRepository;
import com.example.demo.travel.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService{
    final TravelRepository travelRepository;
    final TravelOptionRepository travelOptionRepository;

    @Override
    @Transactional
    public ResponseEntity<TravelDto> createTravel(TravelDto reqForm) {
        Optional<Travel> maybeTravel = travelRepository.findByCity(reqForm.getCity());
        Travel travel;
        if(maybeTravel.isEmpty()) {
            travel = Travel.builder()
                    .city(reqForm.getCity())
                    .country(reqForm.getCountry())
                    .depatureAirport(reqForm.getDepatureAirport())
                    .build();
            List<TravelOptionDto> travelOptionDtoList = reqForm.getAdditionalOptions();
            List<TravelOption> travelOptionList = travelOptionDtoList.stream().map((tod)->new TravelOption(tod, travel)).toList();
            travel.setTravelOptions(travelOptionList);
            travelRepository.save(travel);
        } else {
            travel = maybeTravel.get();
            List<TravelOption> travelOptionList = travel.getTravelOptions();
            List<Long> oldTravelOptionIds = reqForm.getAdditionalOptions().stream().filter((to)->to.getId()!=null).map(TravelOptionDto::getId).toList();
            List<TravelOption> newTravelOptions = reqForm.getAdditionalOptions().stream().filter((to)->to.getId()==null).map(tod->new TravelOption(tod, travel)).toList();
            List<TravelOption> removalTravelOptions = travel.getTravelOptions().stream().filter(to-> !oldTravelOptionIds.contains(to.getId())).toList();

            if(!removalTravelOptions.isEmpty()) {
                travel.getTravelOptions().removeAll(removalTravelOptions);
//                travelOptionRepository.deleteAll(removalTravelOptions);
                travelRepository.save(travel);
            }

            travel.getTravelOptions().addAll(newTravelOptions);
            travelOptionRepository.saveAll(newTravelOptions);
            travelRepository.save(travel);
        }
        TravelDto travelDto = TravelDto.builder()
                .id(travel.getId())
                .city(travel.getCity())
                .country(travel.getCountry())
                .additionalOptions(travel.getTravelOptions().stream().map(to->TravelOptionDto.builder()
                        .optionName(to.getOptionName())
                        .optionPrice(to.getOptionPrice())
                        .isDeprecated(to.getIsDeprecated())
                        .id(to.getId())
                        .build()).toList())
                .build();
        return ResponseEntity.ok(travelDto);
    }

    @Override
    public ResponseEntity<List<String>> getCountries() {
        List<String> responseList = travelRepository.findCountries();
        return ResponseEntity.ok()
                .body(responseList);
    }

    @Override
    public ResponseEntity<List<String>> getCities(String country) {
        List<String> responseList = travelRepository.findCitiesByCountry(country);
        return ResponseEntity.ok()
                .body(responseList);
    }

    @Override
    public ResponseEntity<List<String>> getAirports() {
        List<String> responseList = Arrays.stream(Airport.values()).map(Enum::toString).toList();
        return ResponseEntity.ok(responseList);
    }

}
