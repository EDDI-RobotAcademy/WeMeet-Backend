package com.example.demo.board.service;

import com.example.demo.board.controller.dto.BoardDto;
import org.springframework.http.ResponseEntity;

public interface BoardSerivce {
    ResponseEntity<BoardDto> post(Long moimId, BoardDto req);
}
