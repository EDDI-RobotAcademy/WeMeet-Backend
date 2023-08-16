package com.example.demo.user.controller;

import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.form.UserResForm;
import com.example.demo.user.form.UserSignUpForm;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public boolean userSignout(@RequestHeader HttpHeaders headers, @CookieValue("refreshToken") String refreshToken) {
        log.info("signout()");
        return userService.signOut(headers, refreshToken);
    }
    @GetMapping
    public ResponseEntity getUserInfo() {
        return userService.getUserInfo();
    }

    @GetMapping("/check-nickname")
    public Boolean getNickname(@RequestBody String nickname){
        return userService.checkNickname(nickname);
    }
    @GetMapping("/check-email/{email}")
    public Boolean getEmail(@RequestBody String email){
        return userService.checkNickname(email);
    }
}
