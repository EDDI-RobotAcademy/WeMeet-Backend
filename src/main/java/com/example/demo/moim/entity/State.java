package com.example.demo.moim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private StateType state;
    private LocalDateTime startDate;
    private LocalDateTime runwayStartDate;
    private LocalDateTime takeoffStartDate;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private Integer taxxingPeriod;
    private Integer runwayPeriod;
    private Integer takeoffPeriod;
    @OneToOne(fetch = FetchType.LAZY)
    private Moim moim;
}
