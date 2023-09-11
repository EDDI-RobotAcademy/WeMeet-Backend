package com.example.demo.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class WriterDto {
    private Long id;
    private String nickName;
}
