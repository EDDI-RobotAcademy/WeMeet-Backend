package com.example.demo.payment.repository;

import com.example.demo.payment.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    @Query("select i from Installment i where i.merchantUid=:merchantUid")
    Installment findByMerchantUid(String merchantUid);
}
