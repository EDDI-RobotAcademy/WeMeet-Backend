package com.example.demo.travel.entity;

import com.example.demo.travel.controller.form.TravelOptionReqForm;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TravelOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;
    private Integer optionPrice;

    public TravelOption(TravelOptionReqForm reqForm) {
        this.optionName = reqForm.getOptionName();
        this.optionPrice = reqForm.getOptionPrice();
    }
}
