package com.example.demo.user.controller.form;

import com.example.demo.user.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoResForm {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private RoleType roleType;
}
