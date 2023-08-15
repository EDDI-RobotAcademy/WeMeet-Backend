package com.example.demo.oauth.service;

import com.example.demo.oauth.dto.GoogleOAuthToken;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.User;
import com.example.demo.user.entity.UserRole;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static com.example.demo.user.entity.RoleType.NORMAL;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:google.properties")
public class GoogleServiceImpl implements GoogleService{
    final private UserRepository userRepository;
    final private UserRoleRepository userRoleRepository;
    final private RoleRepository roleRepository;
    @Value("${google.googleLoginUrl}")
    private String googleLoginUrl;
    @Value("${google.GOOGLE_TOKEN_REQUEST_URL}")
    private String GOOGLE_TOKEN_REQUEST_URL;
    @Value("${google.GOOGLE_USERINFO_REQUEST_URL}")
    private String GOOGLE_USERINFO_REQUEST_URL;
    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.redirect-uri}")
    private String googleRedirect_uri;
    @Value("${google.client-secret}")
    private String googleClientSecret;

    public String gooleLoginAddress(){
        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirect_uri
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        System.out.println(reqUrl);
        return reqUrl;
    }
    public GoogleOAuthToken getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Host", "oauth2.googleapis.com");
        headers.add("Content-type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleClientId);
        body.add("redirect_uri", googleRedirect_uri);
        body.add("code", code);
        body.add("client_secret", googleClientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<GoogleOAuthToken> response = restTemplate.exchange(GOOGLE_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, GoogleOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }
    public UserDto saveUserInfo(ResponseEntity<String> response){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }
        String email = (String) jsonMap.get("email");
        String name = (String) jsonMap.get("name");
        String nickname = (String) jsonMap.get("name");
        System.out.println(email);
        User savedUser = userRepository.save(new User(name,nickname,email));
        final RoleType roleType = NORMAL;
        final Role role = roleRepository.findByRoleType(roleType).get();
        final UserRole userRole = new UserRole(savedUser, role);
        userRoleRepository.save(userRole);

        UserDto user = UserDto
                .builder()
                .name(savedUser.getName())
                .build();
        return user;
    }
}


