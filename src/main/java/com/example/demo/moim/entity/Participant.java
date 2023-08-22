package com.example.demo.moim.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Moim moim;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Participant(User user, Moim moim) {
        this.user = user;
        this.moim = moim;
    }
}
