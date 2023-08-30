package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.dto.MoimDto;
import com.example.demo.moim.controller.form.MoimReqForm;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MoimService {
    ResponseEntity<MoimDto> createMoim(MoimReqForm reqForm);
    ResponseEntity<MoimDto> requestMoim(Long id);
    ResponseEntity<Map<String, Object>> joinMoim(Long id);

    ResponseEntity<Map<String, Object>> getRecentMoimList(Integer page, Integer size);

    ResponseEntity<Map<String, Object>> getJoinable(Long id);
}
