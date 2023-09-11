package com.example.demo.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardContentsDto {
    private String title;
    private String content;

}
