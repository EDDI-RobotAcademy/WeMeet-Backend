package com.example.demo.travel.entity;

import com.example.demo.travel.controller.dto.TravelOptionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class TravelOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;
    private Integer optionPrice;
    private Boolean isDeprecated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Travel travel;

    public TravelOption(TravelOptionDto reqForm, Travel travel) {
        this.optionName = reqForm.getOptionName();
        this.optionPrice = reqForm.getOptionPrice();
        this.travel = travel;
        this.isDeprecated = false;
    }
}
