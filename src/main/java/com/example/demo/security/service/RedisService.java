package com.example.demo.security.service;

public interface RedisService {

    void setKeyAndValue(String token, String email, long minutes);
    String getValueByKey(String token);
    void deleteByKey(String token);
}
