package com.example.demo.user.dto;

import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private String name;
    private String nickname;
    private String password;
    private String email;
    private UserRole role;

    @Builder
    public UserDto(String name, String nickname, String password, String email, UserRole role) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
