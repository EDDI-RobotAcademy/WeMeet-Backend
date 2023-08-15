package com.example.demo.oauth.service;

import com.example.demo.oauth.dto.GoogleOAuthToken;
import com.example.demo.user.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String gooleLoginAddress();
    GoogleOAuthToken getAccessToken(String code);
    ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken);
    UserDto saveUserInfo(ResponseEntity<String> response);
}
