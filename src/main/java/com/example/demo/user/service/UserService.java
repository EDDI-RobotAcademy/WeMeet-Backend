package com.example.demo.user.service;

import com.example.demo.user.form.SignOutReqForm;
import com.example.demo.user.form.UserSignUpForm;

public interface UserService {
    boolean signUp(UserSignUpForm userSignUpForm);

    boolean signOut(SignOutReqForm reqForm);
}
