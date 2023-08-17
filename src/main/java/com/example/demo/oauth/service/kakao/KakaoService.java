package com.example.demo.oauth.service.kakao;

import org.springframework.http.ResponseEntity;

public interface KakaoService {
    String kakaoLoginAddress();
    ResponseEntity getJwt(String code);
}
