package com.example.demo.moim.controller.form.token;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class MoimFilterToken {
    private String country;
    private List<String> cityList;
    private List<Integer> totalPrice;
    private List<Integer> numInstallments;
    private Boolean ableTotalPrice;
    private Boolean ableNumInstallments;
}
