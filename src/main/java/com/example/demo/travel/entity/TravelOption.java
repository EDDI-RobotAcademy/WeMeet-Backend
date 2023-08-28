package com.example.demo.travel.entity;

import com.example.demo.travel.controller.form.TravelOptionReqForm;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class TravelOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;
    private Integer optionPrice;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Travel travel;

    public TravelOption(TravelOptionReqForm reqForm) {
        this.optionName = reqForm.getOptionName();
        this.optionPrice = reqForm.getOptionPrice();
    }
}
