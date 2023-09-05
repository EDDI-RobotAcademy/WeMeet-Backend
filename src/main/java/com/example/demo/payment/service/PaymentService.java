package com.example.demo.payment.service;

import com.example.demo.payment.controller.dto.PaymentReqForm;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PaymentService {
    ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId);

    void secondaryPay();

    ResponseEntity<Map<String, Object>> firstPay(Long moimId, PaymentReqForm reqForm);
}
