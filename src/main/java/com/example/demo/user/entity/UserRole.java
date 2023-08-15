package com.example.demo.user.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Role role;

    @Override
    public String toString() {
        return role.toString();
    }

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
