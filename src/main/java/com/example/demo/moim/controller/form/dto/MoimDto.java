package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class MoimDto {
    private Long id;
    private String title;
    private String content;
    private Integer maxNumOfUsers;
    private Integer minNumOfUsers;
    private List<ParticipantDto> participants;
    private Integer currentParticipantsNumber;
    private LocalDateTime createdDate;
}
