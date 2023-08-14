package com.example.demo.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Getter
@PropertySource("classpath:google.properties")
public class GoogleOAuth {
    private final String googleLoginUrl = "https://accounts.google.com";
    private final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.redirect-uri}")
    private String googleRedirect_uri;
    @Value("${google.client-secret}")
    private String googleClientSecret;


}
