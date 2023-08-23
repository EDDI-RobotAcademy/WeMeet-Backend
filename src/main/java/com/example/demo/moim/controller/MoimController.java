package com.example.demo.moim.controller;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMoim(@PathVariable Long id) {
        return moimService.requestMoim(id);
    }
    @PostMapping("/{id}/user")
    public ResponseEntity<Map<String, Object>> participateInMoim(@PathVariable Long id) {
        return moimService.participateInMoim(id);
    }
}