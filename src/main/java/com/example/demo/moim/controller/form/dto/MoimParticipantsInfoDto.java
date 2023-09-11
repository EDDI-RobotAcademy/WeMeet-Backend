package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class MoimParticipantsInfoDto {
    private Long id;
    private List<ParticipantDto> participants;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    private Integer currentParticipantsNumber;
}
