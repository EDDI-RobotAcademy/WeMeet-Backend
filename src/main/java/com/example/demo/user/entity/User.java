package com.example.demo.user.entity;

import com.example.demo.user.controller.form.UserInfoResForm;
import com.example.demo.user.controller.form.UserResForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String password;
    private String email;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserRole userRole;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String nickname, String email, String password) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public User(String name, String nickname, String email) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }
    public RoleType getRole() {
        return this.userRole.getRole().getRoleType();
    }
    public UserInfoResForm toInfoResForm() {
        return UserInfoResForm.builder()
                .email(email)
                .id(id)
                .roleType(this.getRole())
                .nickname(nickname)
                .build();
    }
    public UserResForm toResForm() {
        return new UserResForm(id, nickname);
    }

}
