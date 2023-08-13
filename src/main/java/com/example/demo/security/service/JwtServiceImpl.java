package com.example.demo.security.service;

import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.security.utils.JwtUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService{
    final JwtUtil jwtUtil;
    final RedisService redisService;
    @Override
    public ResponseEntity<Map<String, Object>> refresh() {
        String email = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
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
}
