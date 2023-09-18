package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.dto.MoimDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MoimService {
    ResponseEntity<MoimDto> createMoim(MoimReqForm reqForm);
    ResponseEntity<MoimDto> requestMoim(Long id);
    ResponseEntity<MoimDto> joinMoim(Long id);

    ResponseEntity<List<MoimDto>> getRecentMoimList(Integer page, Integer size);

    ResponseEntity<Map<String, Object>> getJoinable(Long id);

    ResponseEntity<Map<String, Object>> withdrawMoim(Long moimId);

    ResponseEntity<Page<MoimDto>> getAdvanceSerchedList(Integer page, Integer size, String country, String city, String departureAirport, Integer[] rangeTotalPrice, Integer[] rangeNumOfInstallment, Integer[] rangeInstallment, LocalDateTime[] travelDates);
}
