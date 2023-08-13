package com.example.demo.user.form;

import lombok.Getter;

@Getter
public class SignOutReqForm {
    String accessToken;
    String refreshToken;
}
