package com.example.demo.travel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Setter
    private List<TravelOption> travelOptions;
}
