package com.example.demo.security.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.http.HttpResponse;
import java.util.Map;

public interface JwtService {
    ResponseEntity<Map<String, Object>> refresh();
}
