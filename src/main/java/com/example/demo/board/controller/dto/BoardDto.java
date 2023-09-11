package com.example.demo.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String category;
    private BoardContentsDto contents;
    private Boolean isPublic;
}
