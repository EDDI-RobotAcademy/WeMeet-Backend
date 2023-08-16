package com.example.demo.oauth.service.kakao;

import com.example.demo.oauth.dto.KakaoOAuthToken;
import com.example.demo.security.service.JwtService;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.user.entity.RoleType.NORMAL;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:kakao.properties")
public class KakaoServiceImpl implements KakaoService {
    final private UserRepository userRepository;
    final private UserRoleRepository userRoleRepository;
    final private RoleRepository roleRepository;
    final private JwtService jwtService;

    @Value("${kakao.kakaoLoginUrl}")
    private String kakaoLoginUrl;
    @Value("${kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.redirect-uri}")
    private String kakaoRedirect_uri;
    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${kakao.KAKAO_TOKEN_REQUEST_URL}")
    private String KAKAO_TOKEN_REQUEST_URL;
    @Value("${kakao.KAKAO_USERINFO_REQUEST_URL}")
    private String KAKAO_USERINFO_REQUEST_URL;

    @Override
    public String kakaoLoginAddress() {
        String reqUrl = kakaoLoginUrl + "/oauth/authorize?client_id=" + kakaoClientId + "&redirect_uri=" + kakaoRedirect_uri
                + "&response_type=code";
        System.out.println(reqUrl);
        return reqUrl;
    }

    @Override
    public ResponseEntity getJwt(String code) {
        KakaoOAuthToken kakaoOAuthToken = getAccessToken(code);
        ResponseEntity<String> response = requestUserInfo(kakaoOAuthToken);
        User user = saveUserInfo(response);

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(accessToken);
    }

    private User saveUserInfo(ResponseEntity<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        try {
            jsonMap = objectMapper.readValue(response.getBody(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON string", e);
        }

        // "kakao_account" 키 아래의 중첩된 JSON 객체 파싱
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) jsonMap.get("kakao_account");
        String email = (String) kakaoAccountMap.get("email");

        Optional<User> maybeUser = userRepository.findByEmail(email);
        User savedUser;
        if(maybeUser.isEmpty()) {
            String name = (String) jsonMap.get("name");
            String nickname = (String) jsonMap.get("nickname");
            System.out.println(email);
            savedUser = userRepository.save(new User(name, nickname, email));
            final RoleType roleType = NORMAL;
            final Role role = roleRepository.findByRoleType(roleType).get();
            final UserRole userRole = new UserRole(savedUser, role);
            userRoleRepository.save(userRole);
        } else {
            savedUser = maybeUser.get();
        }
        return savedUser;
    }

    private ResponseEntity<String> requestUserInfo(KakaoOAuthToken kakaoOAuthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoOAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET, request, String.class);
        System.out.println("response.getBody() = " + response.getBody());
        return response;
    }

    private KakaoOAuthToken getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirect_uri);
        body.add("code", code);
        body.add("client_secret", kakaoClientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoOAuthToken> response = restTemplate.exchange(KAKAO_TOKEN_REQUEST_URL, HttpMethod.POST, tokenRequest, KakaoOAuthToken.class);
        System.out.println(response);
        System.out.println(response.getBody().getAccess_token());
        return response.getBody();
    }
}
