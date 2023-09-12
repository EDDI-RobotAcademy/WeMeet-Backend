package com.example.demo.board.controller.dto;

import com.example.demo.board.entity.Writer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class BoardDto {
    private String title;
    private Long id;
    private String category;
    private BoardContentsDto contents;
    private Boolean isPublic;
    private Map<String, Object> additionalInfo;
    private WriterDto writer;
}
