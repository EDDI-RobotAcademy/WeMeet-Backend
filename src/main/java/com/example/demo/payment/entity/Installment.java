package com.example.demo.payment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Long amount;
    private String merchantUid;
    @Setter
    private String impUid;
    @Setter
    private String receipt_url;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;
}
