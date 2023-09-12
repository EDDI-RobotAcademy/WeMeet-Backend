package com.example.demo.moim.controller;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.dto.MoimDto;
import com.example.demo.moim.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moim")
@RequiredArgsConstructor
@Slf4j
public class MoimController {
    final MoimService moimService;

    @PostMapping
    public ResponseEntity<MoimDto> createMoim(@RequestBody MoimReqForm reqForm) {
        log.info("createMoim()");
        return moimService.createMoim(reqForm);
    }

    @GetMapping("/{moimId}")
    public ResponseEntity<MoimDto> getMoim(@PathVariable Long moimId) {
        return moimService.requestMoim(moimId);
    }
    @PostMapping("/{moimId}/user")
    public ResponseEntity<MoimDto> JoinMoim(@PathVariable Long moimId) {
        return moimService.joinMoim(moimId);
    }

    @DeleteMapping("/{moimId}/user")
    public ResponseEntity<Map<String, Object>> withdrawMoim(@PathVariable Long moimId) {
        return moimService.withdrawMoim(moimId);
    }

    @GetMapping(value = "/list", params = {"page", "size"})
    public ResponseEntity<List<MoimDto>> getRecentMoimList(@RequestParam Integer page, @RequestParam Integer size) {
        log.info("getRecentMoimList");
        return moimService.getRecentMoimList(page, size);
    }

    @GetMapping("/{id}/joinable")
    public ResponseEntity<Map<String, Object>> getJoinable(@PathVariable Long id) {
        log.info("getJoinable()");
        return moimService.getJoinable(id);
    }

}
