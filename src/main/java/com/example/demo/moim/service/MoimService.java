package com.example.demo.moim.service;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.MoimResForm;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MoimService {
    ResponseEntity<Map<String, Object>> createMoim(MoimReqForm reqForm);
    ResponseEntity<Map<String, Object>> requestMoim(Long id);
    ResponseEntity<Map<String, Object>> participateInMoim(Long id);

    ResponseEntity<Map<String, Object>> getRecentMoimList(Integer page, Integer size);
}
