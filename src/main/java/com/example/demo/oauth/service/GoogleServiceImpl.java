package com.example.demo.oauth.service;

import com.example.demo.oauth.GoogleOAuth;
import com.example.demo.oauth.entity.GoogleOAuthToken;
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
public class GoogleServiceImpl implements GoogleService{
    final private GoogleOAuth googleOAuth;
    final private UserRepository userRepository;
    final private UserRoleRepository userRoleRepository;
    final private RoleRepository roleRepository;
    public GoogleOAuthToken getAccessToken(String code) {
        String URL = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Host", "oauth2.googleapis.com");
        headers.add("Content-type", "application/x-www-form-urlencoded");


        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", googleOAuth.getGoogleClientId());
        body.add("redirect_uri", googleOAuth.getGoogleRedirect_uri());
        body.add("code", code);
        body.add("client_secret", googleOAuth.getGoogleClientSecret());

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<GoogleOAuthToken> response = restTemplate.exchange(URL, HttpMethod.POST, tokenRequest, GoogleOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }

    public ResponseEntity<String> requestUserInfo(GoogleOAuthToken oAuthToken) {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v1/userinfo";

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


