package com.example.demo.moim.entity;

import com.example.demo.payment.entity.Payment;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY)
    private MoimParticipantsInfo moimParticipantsInfo;
}
