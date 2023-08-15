package com.example.demo.oauth.controller;

import com.example.demo.oauth.service.GoogleService;
import com.example.demo.oauth.dto.GoogleOAuthToken;
import com.example.demo.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OauthController {
    final private GoogleService googleService;

    @GetMapping("/google")
    public @ResponseBody String getGoogleOAuthUrl(HttpServletRequest request) throws Exception {
        return googleService.gooleLoginAddress();
    }

    @GetMapping("/google-login")
    public UserDto googleCallback(@RequestParam String code) {
        GoogleOAuthToken googleOAuthToken = googleService.getAccessToken(code);
        ResponseEntity<String> response = googleService.requestUserInfo(googleOAuthToken);
        return googleService.saveUserInfo(response);
    }

}
