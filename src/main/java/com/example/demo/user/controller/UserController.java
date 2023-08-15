package com.example.demo.user.controller;

import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.form.SignOutReqForm;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    final private UserService userService;
    @PostMapping("/sign-up")
    public boolean userSignUp(@RequestBody UserSignUpForm userSignUpForm){
        return userService.signUp(userSignUpForm);
    }
    @PostMapping("/sign-out")
    public boolean userSignout(@RequestBody SignOutReqForm reqForm) {
        log.info("signout()");
        return userService.signOut(reqForm);
    }
    @GetMapping
    public boolean test() {
        log.info("test");
        log.info(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return true;
    }
}
