package com.example.demo.moim.entity;

import com.example.demo.moim.controller.form.MoimReqForm;
import com.example.demo.moim.controller.form.MoimInfoResForm;
import com.example.demo.moim.controller.form.MoimResForm;
import com.example.demo.travel.entity.Travel;
import com.example.demo.travel.entity.TravelOption;
import jakarta.persistence.*;
import lombok.*;
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
@Setter
public class Moim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "moim")
    private List<Participant> participants;
    @Formula("(select count(1) from participant p where p.moim_id = id)")
    private Integer currentParticipantsNumber;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "moim")
    private MoimDestination destination;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "moim")
    private List<MoimOption> options;
}
