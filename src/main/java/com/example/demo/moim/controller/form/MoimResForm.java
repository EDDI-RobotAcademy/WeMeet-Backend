package com.example.demo.moim.controller.form;

import com.example.demo.moim.entity.Participant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class MoimResForm {
    private Long id;
    private String title;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    private Integer currentParticipantsNumber;
}
