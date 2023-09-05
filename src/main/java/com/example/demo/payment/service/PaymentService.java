package com.example.demo.payment.service;

import com.example.demo.payment.controller.dto.PaymentReqForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface PaymentService {
    ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId);

    void secondaryPay();

    ResponseEntity<Map<String, Object>> firstPay(Long moimId, PaymentReqForm reqForm);
    ResponseEntity<Map<String, Object>> webHook(@RequestBody Map req);
}
