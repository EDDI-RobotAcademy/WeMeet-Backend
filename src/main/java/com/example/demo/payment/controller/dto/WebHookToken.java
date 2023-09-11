package com.example.demo.payment.controller.dto;

import lombok.Getter;

@Getter
public class WebHookToken {
    private String imp_uid;
    private String merchant_uid;
    private String status;
}
