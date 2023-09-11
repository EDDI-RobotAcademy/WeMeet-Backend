package com.example.demo.user.controller.form;

import com.example.demo.user.entity.UserRole;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private UserRole userRole;
}
