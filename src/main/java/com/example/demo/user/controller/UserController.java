package com.example.demo.user.controller;

import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    final private UserService userService;
    @PostMapping("/sign-up")
    public boolean userSighUp(@RequestBody UserSignUpForm userSignUpForm){
        return userService.signUp(userSignUpForm);
    }
}
