package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.dto.MoimDestinationDto;
import com.example.demo.moim.controller.form.dto.MoimDto;
import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.dto.MoimOptionDto;
import com.example.demo.moim.controller.form.dto.ParticipantDto;
import com.example.demo.moim.entity.*;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.moim.repository.ParticipantRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.travel.repository.TravelRepository;
import com.example.demo.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoimServiceImpl implements MoimService {
    final MoimRepository moimRepository;
    final ParticipantRepository participantRepository;
    final TravelRepository travelRepository;

    @Override
    @Transactional
    public ResponseEntity<MoimDto> createMoim(MoimReqForm reqForm) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Moim moim = Moim.builder()
                .title(reqForm.getBasicInfo().getTitle())
                .content(reqForm.getBasicInfo().getContent())
                .maxNumOfUsers(reqForm.getParticipantsInfo().getMaxParticipants())
                .minNumOfUsers(reqForm.getParticipantsInfo().getMinParticipants())
                .participants(new ArrayList<>())
                .build();

        moim.setDestination(MoimDestination.builder()
                .country(reqForm.getDestinationInfo().getCountry())
                .city(reqForm.getDestinationInfo().getCity())
                .moim(moim)
                .build());

        moim.setOptions(reqForm.getOptionsInfo().stream()
                .map((oi) -> MoimOption.builder()
                        .optionName(oi.getOptionName())
                        .optionPrice(oi.getOptionPrice())
                        .moim(moim)
                        .build())
                .toList());
        moim.getParticipants().add(new Participant(user, moim));

        moim.setState(State.builder()
                        .runwayStartDate(reqForm.getStateInfo().getRunwayStartDate())
                        .takeoffStartDate(reqForm.getStateInfo().getTakeoffStartDate())
                        .startDate(reqForm.getStateInfo().getStartDate())
                        .departureDate(reqForm.getStateInfo().getDepartureDate())
                        .taxxingPeriod(reqForm.getStateInfo().getTaxxingPeriod())
                        .runwayPeriod(reqForm.getStateInfo().getRunwayPeriod())
                        .takeoffPeriod(reqForm.getStateInfo().getTakeoffPeriod())
                        .state(StateType.TAXXING)
                .build());
        moimRepository.save(moim);

        MoimDto moimDto = MoimDto.builder()
                .id(moim.getId())
                .content(moim.getContent())
                .maxNumOfUsers(moim.getMaxNumOfUsers())
                .minNumOfUsers(moim.getMinNumOfUsers())
                .createdDate(moim.getCreatedDate())
                .currentParticipantsNumber(moim.getCurrentParticipantsNumber())
                .participants(List.of(ParticipantDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .build()))
                .build();
        return ResponseEntity.ok(moimDto);
    }

    @Override
    @Transactional
    public ResponseEntity<MoimDto> requestMoim(Long id) {
        Optional<Moim> savedMoim = moimRepository.findById(id);
        if (savedMoim.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        } else {
            Moim moim = savedMoim.get();
            MoimDto moimDto = MoimDto.builder()
                    .id(moim.getId())
                    .content(moim.getContent())
                    .maxNumOfUsers(moim.getMaxNumOfUsers())
                    .minNumOfUsers(moim.getMinNumOfUsers())
                    .createdDate(moim.getCreatedDate())
                    .currentParticipantsNumber(moim.getCurrentParticipantsNumber())
                    .participants(
                            participantRepository.findAllByMoim(moim).stream()
                                    .map((p) -> ParticipantDto.builder()
                                            .id(p.getUser().getId())
                                            .nickname(p.getUser().getNickname())
                                            .build())
                                    .toList()
                    )
                    .destination(MoimDestinationDto.builder()
                            .id(moim.getDestination().getId())
                            .city(moim.getDestination().getCity())
                            .country(moim.getDestination().getCountry())
                            .build())
                    .options(moim.getOptions().stream()
                            .map((o)-> MoimOptionDto.builder()
                                    .id(o.getId())
                                    .optionName(o.getOptionName())
                                    .optionPrice(o.getOptionPrice())
                                    .build())
                            .toList())
                    .build();
            return ResponseEntity.ok(moimDto);
        }
    }

    @Override
    public ResponseEntity<MoimDto> joinMoim(Long id) {
        Optional<Moim> savedMoim = moimRepository.findById(id);
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (savedMoim.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            Moim moim = savedMoim.get();
            Participant participant = new Participant(user, moim);
            moim.getParticipants().add(participant);
            participantRepository.save(participant);
            moimRepository.save(moim);
            MoimDto moimDto = MoimDto.builder()
                    .title(moim.getTitle())
                    .content(moim.getContent())
                    .maxNumOfUsers(moim.getMaxNumOfUsers())
                    .minNumOfUsers(moim.getMinNumOfUsers())
                    .id(moim.getId())
                    .createdDate(moim.getCreatedDate())
                    .currentParticipantsNumber(moim.getCurrentParticipantsNumber())
                    .build();
            return ResponseEntity.ok()
                    .body(moimDto);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<List<MoimDto>> getRecentMoimList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<Moim> moimList = moimRepository.findRecentPageableMoim(pageable);
        List<MoimDto> responseList = moimList.stream()
                .map((m) ->
                        MoimDto.builder()
                                .id(m.getId())
                                .title(m.getTitle())
                                .content(m.getContent())
                                .minNumOfUsers(m.getMinNumOfUsers())
                                .maxNumOfUsers(m.getMaxNumOfUsers())
                                .currentParticipantsNumber(m.getCurrentParticipantsNumber())
                                .createdDate(m.getCreatedDate())
                                .build()
                ).toList();

        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<Map<String, Object>> getJoinable(Long id) {
        Optional<Moim> maybeMoim = moimRepository.findById(id);
        if (maybeMoim.isEmpty()) {
            return ResponseEntity.noContent()
                    .build();
        }
        Moim moim = maybeMoim.get();
        Map<String, Object> responseMap;
        if (moim.getCurrentParticipantsNumber() < moim.getMaxNumOfUsers()) {
            responseMap = Map.of("joinable", true);
        } else {
            responseMap = Map.of("joinable", false);
        }
        return ResponseEntity.ok()
                .body(responseMap);
    }
}
