package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.dto.*;
import com.example.demo.moim.controller.form.moimReqForm.OptionInfo;
import com.example.demo.moim.controller.form.moimReqForm.ParticipantsInfo;
import com.example.demo.moim.entity.*;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.moim.repository.ParticipantRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.travel.entity.Airport;
import com.example.demo.travel.repository.TravelRepository;
import com.example.demo.user.controller.form.UserDto;
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

        MoimContents moimContents = MoimContents.builder()
                .title(reqForm.getBasicInfo().getTitle())
                .content(reqForm.getBasicInfo().getContent())
                .build();

        State state = State.builder()
                .runwayStartDate(reqForm.getStateInfo().getRunwayStartDate())
                .takeoffStartDate(reqForm.getStateInfo().getTakeoffStartDate())
                .startDate(reqForm.getStateInfo().getStartDate())
                .departureDate(reqForm.getStateInfo().getDepartureDate())
                .taxxingPeriod(reqForm.getStateInfo().getTaxxingPeriod())
                .runwayPeriod(reqForm.getStateInfo().getRunwayPeriod())
                .takeoffPeriod(reqForm.getStateInfo().getTakeoffPeriod())
                .returnDate(reqForm.getStateInfo().getReturnDate())
                .state(StateType.TAXXING)
                .build();

        List<MoimOption> options = reqForm.getOptionsInfo().stream()
                .map((oi) -> MoimOption.builder()
                        .optionName(oi.getOptionName())
                        .optionPrice(oi.getOptionPrice()).build()).toList();

        MoimDestination moimDestination = MoimDestination.builder()
                .country(reqForm.getDestinationInfo().getCountry())
                .city(reqForm.getDestinationInfo().getCity())
                .departureAirport(Airport.valueOf(reqForm.getDestinationInfo().getDepartureAirport()))
                .moimOptions(options)
                .build();

        MoimPaymentInfo paymentInfo = MoimPaymentInfo.builder()
                .totalPrice(reqForm.getOptionsInfo().stream().map(OptionInfo::getOptionPrice).reduce(Long::sum).orElse(0L))
                .numInstallments(reqForm.getStateInfo().getRunwayPeriod())
                .build();
        paymentInfo.setAmountInstallment(paymentInfo.getTotalPrice()/ paymentInfo.getNumInstallments());

        MoimParticipantsInfo participantsInfo = MoimParticipantsInfo.builder()
                .maxNumOfUsers(reqForm.getParticipantsInfo().getMaxParticipants())
                .minNumOfUsers(reqForm.getParticipantsInfo().getMinParticipants())
                .participants(new ArrayList<>())
                .build();

        Moim moim = Moim.builder()
                .contents(moimContents)
                .moimPaymentInfo(paymentInfo)
                .participantsInfo(participantsInfo)
                .state(state)
                .destination(moimDestination)
                .build();

        moimDestination.setMoim(moim);
        paymentInfo.setMoim(moim);
        participantsInfo.setMoim(moim);
        state.setMoim(moim);
        moimRepository.save(moim);

        joinMoim(moim.getId());

        MoimDto moimDto = MoimDto.builder()
                .id(moim.getId())
                .build();
        return ResponseEntity.ok(moimDto);
    }

    @Override
    @Transactional
    public ResponseEntity<MoimDto> requestMoim(Long id) {
        Optional<Moim> maybeMoim = moimRepository.findById(id);
        if (maybeMoim.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();

        } else {
            Moim savedMoim = maybeMoim.get();
            MoimDestinationDto moimDestinationDto = MoimDestinationDto.builder()
                    .country(savedMoim.getDestination().getCountry())
                    .city(savedMoim.getDestination().getCity())
                    .departureAirport(savedMoim.getDestination().getDepartureAirport())
                    .options(savedMoim.getDestination().getMoimOptions().stream().map((o)-> MoimOptionDto.builder()
                            .id(o.getId())
                            .optionName(o.getOptionName())
                            .optionPrice(o.getOptionPrice())
                            .build()).toList())
                    .build();

            MoimContentsDto contentsDto = MoimContentsDto.builder()
                    .id(savedMoim.getContents().getId())
                    .title(savedMoim.getContents().getTitle())
                    .content(savedMoim.getContents().getContent())
                    .build();

            MoimPaymentInfoDto paymentInfoDto = MoimPaymentInfoDto.builder()
                    .id(savedMoim.getMoimPaymentInfo().getId())
                    .amountInstallment(savedMoim.getMoimPaymentInfo().getAmountInstallment())
                    .totalPrice(savedMoim.getMoimPaymentInfo().getTotalPrice())
                    .build();

            State state = savedMoim.getState();
            StateDto stateDto = StateDto.builder()
                    .id(state.getId())
                    .returnDate(state.getReturnDate())
                    .taxxingPeriod(state.getTaxxingPeriod())
                    .takeoffPeriod(state.getTakeoffPeriod())
                    .runwayPeriod(state.getRunwayPeriod())
                    .departureDate(state.getDepartureDate())
                    .startDate(state.getDepartureDate())
                    .takeoffStartDate(state.getTakeoffStartDate())
                    .runwayStartDate(state.getStartDate())
                    .build();

            MoimParticipantsInfo participantsInfo = savedMoim.getParticipantsInfo();
            MoimParticipantsInfoDto participantsInfoDto = MoimParticipantsInfoDto.builder()
                    .maxNumOfUsers(participantsInfo.getMaxNumOfUsers())
                    .minNumOfUsers(participantsInfo.getMinNumOfUsers())
                    .participants(participantsInfo.getParticipants().stream().map((p)-> ParticipantDto.builder()
                            .id(p.getId())
                            .user(UserDto.builder()
                                    .id(p.getUser().getId())
                                    .name(p.getUser().getName())
                                    .nickname(p.getUser().getNickname())
                                    .email(p.getUser().getEmail())
                                    .build())
                            .build()).toList())
                    .currentParticipantsNumber(participantsInfo.getCurrentParticipantsNumber())
                    .build();

            MoimDto moimDto = MoimDto.builder()
                    .id(savedMoim.getId())
                    .moimDestination(moimDestinationDto)
                    .state(stateDto)
                    .paymentInfo(paymentInfoDto)
                    .moimContents(contentsDto)
                    .createdDate(savedMoim.getCreatedDate())
                    .moimParticipantsInfo(participantsInfoDto)
                    .build();
            return ResponseEntity.ok(moimDto);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MoimDto> joinMoim(Long id) {
        Optional<Moim> savedMoim = moimRepository.findById(id);
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        if (savedMoim.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } else {
            Moim moim = savedMoim.get();
            Participant participant = Participant.builder()
                    .user(user)
                    .moimParticipantsInfo(moim.getParticipantsInfo())
                    .build();
            participantRepository.save(participant);

            MoimParticipantsInfo participantsInfo = moim.getParticipantsInfo();
            participantsInfo.getParticipants().add(participant);
            moimRepository.save(moim);
            return requestMoim(id);
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
                                .moimDestination(MoimDestinationDto.builder()
                                        .departureAirport(m.getDestination().getDepartureAirport())
                                        .city(m.getDestination().getCity())
                                        .country(m.getDestination().getCountry())
                                        .build())
                                .moimContents(MoimContentsDto.builder()
                                        .content(m.getContents().getContent())
                                        .title(m.getContents().getTitle())
                                        .build())
                                .moimParticipantsInfo(MoimParticipantsInfoDto.builder()
                                        .minNumOfUsers(m.getParticipantsInfo().getMinNumOfUsers())
                                        .maxNumOfUsers(m.getParticipantsInfo().getMaxNumOfUsers())
                                        .currentParticipantsNumber(m.getParticipantsInfo().getCurrentParticipantsNumber())
                                        .build())
                                .paymentInfo(MoimPaymentInfoDto.builder()
                                        .numInstallments(m.getMoimPaymentInfo().getNumInstallments())
                                        .totalPrice(m.getMoimPaymentInfo().getTotalPrice())
                                        .amountInstallment(m.getMoimPaymentInfo().getAmountInstallment())
                                        .build())
                                .build()
                ).toList();

        return ResponseEntity.ok(responseList);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> getJoinable(Long id) {
        Optional<Moim> maybeMoim = moimRepository.findById(id);
        if (maybeMoim.isEmpty()) {
            return ResponseEntity.noContent()
                    .build();
        }
        Moim moim = maybeMoim.get();
        Map<String, Object> responseMap;
        if (moim.getParticipantsInfo().getCurrentParticipantsNumber() < moim.getParticipantsInfo().getMaxNumOfUsers()) {
            responseMap = Map.of("joinable", true);
        } else {
            responseMap = Map.of("joinable", false);
        }
        return ResponseEntity.ok()
                .body(responseMap);
    }
}
