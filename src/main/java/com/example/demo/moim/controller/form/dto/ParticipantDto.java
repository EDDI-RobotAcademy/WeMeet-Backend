package com.example.demo.moim.controller.form.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantDto {
    private Long id;
    private String name;
    private String nickname;
    private String email;
}
