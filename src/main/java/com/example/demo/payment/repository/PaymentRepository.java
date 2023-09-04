package com.example.demo.payment.repository;

import com.example.demo.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.participant.moim.id = :moimId")
    Payment findByMoimId(Long moimId);
}
