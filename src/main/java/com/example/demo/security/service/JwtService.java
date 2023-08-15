package com.example.demo.security.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public interface JwtService {
    ResponseEntity<Map<String, Object>> refresh(String refreshToken);
}
