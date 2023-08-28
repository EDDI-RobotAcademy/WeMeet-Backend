package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.MoimReqForm;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MoimService {
    ResponseEntity<Map<String, Object>> createMoim(MoimReqForm reqForm);
    ResponseEntity<Map<String, Object>> requestMoim(Long id);
    ResponseEntity<Map<String, Object>> participateInMoim(Long id);

    ResponseEntity<Map<String, Object>> getRecentMoimList(Integer page, Integer size);

    ResponseEntity<Map<String, Object>> getJoinable(Long id);
}
