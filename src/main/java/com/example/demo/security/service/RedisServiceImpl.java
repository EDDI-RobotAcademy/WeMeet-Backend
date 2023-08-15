package com.example.demo.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    final private StringRedisTemplate redisTemplate;

    @Override
    public void setKeyAndValue(String token, String email, long minutes) {
        String accountIdToString = String.valueOf(email);
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        value.set(token, accountIdToString, Duration.ofMinutes(minutes));
    }

    @Override
    public String getValueByKey(String token) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        String tmpUsername = value.get(token);
        String username;

        if (tmpUsername == null) {
            username = null;
        } else {
            username = (tmpUsername);
        }

        return username;
    }

    @Override
    public void deleteByKey(String token) {
        redisTemplate.delete(token);
    }

    public Boolean isRefreshTokenExists(String token) {
        return getValueByKey(token) != null;
    }
}
