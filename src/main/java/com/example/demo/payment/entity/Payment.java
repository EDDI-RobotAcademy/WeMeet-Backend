package com.example.demo.payment.entity;

import com.example.demo.moim.entity.MoimPaymentInfo;
import com.example.demo.moim.entity.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment", cascade = CascadeType.PERSIST)
    private Participant participant;

    private Integer numInstallments;
    private String customerUid;
    private String pgProvider;
    private String payMethod;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "payment")
    private List<Installment> installments;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private MoimPaymentInfo paymentInfo;
}
