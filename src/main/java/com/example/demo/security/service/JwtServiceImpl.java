package com.example.demo.security.service;

import com.example.demo.security.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService{
    final JwtUtil jwtUtil;
    final RedisService redisService;
    final long ACCESS_TOKEN_EXP_MIN = 10;
    final long REFRESH_TOKEN_EXP_MIN = 10;

    @Override
    public ResponseEntity<Map<String, Object>> refresh(String refreshToken) {

        Map<String, Object> values = jwtUtil.validateToken(refreshToken);
        String email = (String) values.get("email");

        Map<String, Object> payload = Map.of("email", email);
        long accessTokenExpMin = 10;
        String accessToken = jwtUtil.generateToken(payload, accessTokenExpMin);
        redisService.setKeyAndValue(accessToken, email, accessTokenExpMin+1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("accessToken", accessToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(responseMap);
    }
    @Override
    public String generateAccessToken(String email) {
        String accessToken = jwtUtil.generateToken(Map.of("email", email), ACCESS_TOKEN_EXP_MIN);
        redisService.setKeyAndValue(accessToken, email, ACCESS_TOKEN_EXP_MIN+1);
        return accessToken;
    }
    @Override
    public String generateRefreshToken(String email) {
        String refreshToken = jwtUtil.generateToken(Map.of("email", email), REFRESH_TOKEN_EXP_MIN);
        redisService.setKeyAndValue(refreshToken, email, REFRESH_TOKEN_EXP_MIN+1);
        return refreshToken;
    }
}
