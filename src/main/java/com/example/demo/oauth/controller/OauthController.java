package com.example.demo.oauth.controller;

import com.example.demo.oauth.service.google.GoogleService;
import com.example.demo.oauth.service.kakao.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {
    final private GoogleService googleService;
    final private KakaoService kakaoService;

    @GetMapping("/google")
    public @ResponseBody String getGoogleOAuthUrl(HttpServletRequest request) throws Exception {
        return googleService.gooleLoginAddress();
    }

    @GetMapping("/google-login")
    public ResponseEntity googleCallback(@RequestParam String code) {
        return googleService.getJwt(code);
    }

    @GetMapping("/kakao")
    public String getKakaoOAuthUrl() {
        log.info("getKakaoOAuthUrl()");
        return kakaoService.kakaoLoginAddress();
    }

    @GetMapping("/kakao-login")
    public ResponseEntity kakaoCallback(@RequestParam String code) {
        log.info("kakaoCallback()");
        return kakaoService.getJwt(code);
    }
}
