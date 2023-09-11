package com.example.demo.moim.repository;

import com.example.demo.moim.entity.MoimPaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentInfoRepository extends JpaRepository<MoimPaymentInfo, Long> {
    @Query("select sum(o.optionPrice)/m.moimPaymentInfo.amountInstallment from Moim m join m.destination.moimOptions o where m.id=:moimId")
    Integer calculateNumInstallments(Long moimId);
}
