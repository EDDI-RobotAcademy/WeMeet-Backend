package com.example.demo.payment.repository;

import com.example.demo.moim.entity.Moim;
import com.example.demo.moim.entity.Participant;
import com.example.demo.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.paymentInfo.moim.id = :moimId")
    Payment findByMoimId(Long moimId);

    @Query("select p from Payment p where p.customerUid = :impUid")
    Payment findByImpUid(String impUid);

    @Query("select p from Payment p " +
            "join fetch p.installments " +
            "where p.participant =:participant and p.participant.moimParticipantsInfo.moim =:moim")
    Payment findByParticipantAndMoim(Participant participant, Moim moim);
}
