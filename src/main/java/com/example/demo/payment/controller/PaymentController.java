package com.example.demo.payment.controller;

import com.example.demo.payment.controller.dto.PaymentReqForm;
import com.example.demo.payment.service.PaymentService;
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
    @PostMapping("/moim/{moimId}")
    public ResponseEntity<Map<String, Object>> firstPay(@PathVariable Long moimId, @RequestBody PaymentReqForm reqForm) {
        return paymentService.firstPay(moimId, reqForm);
    }

    @GetMapping
    public void secondaryPay() {
        paymentService.secondaryPay();
    }
}
