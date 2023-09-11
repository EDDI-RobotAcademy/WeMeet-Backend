package com.example.demo.board.service;

import com.example.demo.board.controller.dto.BoardDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BoardSerivce {
    ResponseEntity<BoardDto> post(Long moimId, BoardDto req);

    ResponseEntity<List<BoardDto>> getMoimBoardList(Long moimId, Integer page, Integer size);

    ResponseEntity<BoardDto> getBoard(Long boardId, String category);
}
