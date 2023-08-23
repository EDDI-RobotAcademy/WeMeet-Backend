package com.example.demo.moim.entity;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.MoimInfoResForm;
import com.example.demo.moim.controller.form.MoimResForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "moim")
    private List<Participant> participants;
    @Formula("(select count(1) from participant p where p.moim_id = id)")
    private Integer currentParticipantsNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;

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
    public MoimResForm toResForm() {
        return MoimResForm.builder()
                .id(id)
                .title(title)
                .maxNumOfUsers(maxNumOfUsers)
                .minNumOfUsers(minNumOfUsers)
                .currentParticipantsNumber(currentParticipantsNumber)
                .build();
    }
}
