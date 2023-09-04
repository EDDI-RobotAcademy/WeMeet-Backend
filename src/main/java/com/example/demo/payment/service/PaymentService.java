package com.example.demo.payment.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PaymentService {
    ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId);
}
