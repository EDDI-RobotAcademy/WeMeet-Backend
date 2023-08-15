package com.example.demo.user.service;

import com.example.demo.user.form.UserSignUpForm;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserService {
    boolean signUp(UserSignUpForm userSignUpForm);

    boolean signOut(HttpHeaders headers, String refreshToken);
}
