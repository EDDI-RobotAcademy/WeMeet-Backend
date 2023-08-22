package com.example.demo.user.controller.form;

import com.example.demo.user.entity.RoleType;
import com.example.demo.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResForm {
    private String name;
    private String nickname;
    private String email;
    private RoleType roleType;
}
