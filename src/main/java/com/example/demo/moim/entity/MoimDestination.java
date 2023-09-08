package com.example.demo.moim.entity;

import com.example.demo.travel.entity.Airport;
import com.example.demo.travel.entity.Travel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoimDestination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String city;
    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    private Moim moim;
    @Enumerated(value = EnumType.STRING)
    private Airport departureAirport;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MoimOption> moimOptions;
}
