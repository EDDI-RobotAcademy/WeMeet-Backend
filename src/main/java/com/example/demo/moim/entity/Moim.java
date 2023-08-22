package com.example.demo.moim.entity;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.MoimInfoResForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Moim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Participant> participants;

    public static Moim toMoim(MoimReqForm reqForm) {
        return Moim.builder()
                .title(reqForm.getTitle())
                .maxNumOfUsers(reqForm.getMaxNumOfUsers())
                .minNumOfUsers(reqForm.getMinNumOfUsers())
                .participants(new ArrayList<>())
                .build();
    }
    public MoimInfoResForm toInfoResForm() {
        return MoimInfoResForm.builder()
                .id(id)
                .title(title)
                .maxNumOfUsers(maxNumOfUsers)
                .minNumOfUsers(minNumOfUsers)
                .participants(participants.stream().map((pu)->pu.getUser().toResForm()).toList())
                .build();
    }
}
