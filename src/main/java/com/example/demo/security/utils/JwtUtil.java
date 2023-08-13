package com.example.demo.security.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secrets}")
    private String key;
    @Value("${uri.exclude.tokenCheckFilter}")
    private List<String> tokenCheckFilterExcludeUris;

    public boolean isTokenCheckFilterExcludeUris(String uri) {
        for (String excludeUri: tokenCheckFilterExcludeUris) {
            if(uri.startsWith(excludeUri)) {
                return true;
            }
        }
        return false;
    }

    public String generateToken(Map<String, Object> valueMap, long minutes) {
        //헤더 부분
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 부분 설정
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(minutes).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    public Map<String, Object> validateToken(String token) throws SignatureException, ExpiredJwtException {
        Map<String, Object> claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }
}
