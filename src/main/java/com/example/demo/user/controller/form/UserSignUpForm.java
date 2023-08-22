package com.example.demo.user.controller.form;

import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignUpForm {

    final private String name;
    final private String nickname;
    final private String email;
    final private String password;
    final private RoleType roleType;

    public User toUser(String password) {
        return new User(name, nickname, email, password);
    }

}
