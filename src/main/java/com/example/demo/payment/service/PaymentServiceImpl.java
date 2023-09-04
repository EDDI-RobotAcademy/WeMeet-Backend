package com.example.demo.payment.service;

import com.example.demo.payment.entity.Payment;
import com.example.demo.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    final PaymentRepository paymentRepository;
    @Override
    public ResponseEntity<Map<String, Object>> getPaymentInfo(Long moimId) {
        Payment payment = paymentRepository.findByMoimId(moimId);
        Map<String, Object> responseMap = Map.of("totalPrice", payment.getTotalPrice());
        return ResponseEntity.ok(responseMap);
    }
}
