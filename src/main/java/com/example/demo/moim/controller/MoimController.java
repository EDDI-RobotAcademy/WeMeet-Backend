package com.example.demo.moim.controller;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/moim")
@RequiredArgsConstructor
@Slf4j
public class MoimController {
    final MoimService moimService;
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMoim(@RequestBody MoimReqForm reqForm) {
        log.info("createMoim()");
        return moimService.createMoim(reqForm);
    }
}
