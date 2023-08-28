package com.example.demo.travel.controller.form;

import lombok.Getter;

import java.util.List;

@Getter
public class TravelReqForm {
    private String country;
    private String city;
    private List<TravelOptionReqForm> additionalOptions;
}
