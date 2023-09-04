package com.example.demo.payment.controller;

import com.example.demo.payment.service.PaymentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    final PaymentService paymentService;
    @GetMapping(value = "/moim/{moimId}")
    public ResponseEntity<Map<String, Object>> getPaymentInfo(@PathVariable Long moimId) {
        return paymentService.getPaymentInfo(moimId);
    }
    @PostMapping
    public ResponseEntity<Map<String, Object>> firstPay() {
        return null;
    }
}
