package com.example.demo.oauth.service.google;

import com.example.demo.oauth.dto.GoogleOAuthToken;
import com.example.demo.user.entity.User;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    String gooleLoginAddress();
    ResponseEntity getJwt(String code);
}
