package com.example.demo.moim.controller.form.dto;

import com.example.demo.user.controller.form.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ParticipantDto {
    private UserDto user;
    private Long id;
}
