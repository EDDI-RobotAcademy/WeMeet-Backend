package com.example.demo.travel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    @Enumerated(EnumType.STRING)
    private Airport depatureAirport;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "travel", orphanRemoval = true)
    @Setter
    private List<TravelOption> travelOptions;
}
