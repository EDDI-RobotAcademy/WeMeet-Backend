package com.example.demo.payment.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PaymentReqForm {

    @JsonProperty("customer_uid")
    private String customerUid;
    @JsonProperty("merchant_uid")
    private String merchantUid;
    private String payMethod;
    private String pgProvider;

}
