package com.example.demo.board.service;

import com.example.demo.board.controller.dto.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    ResponseEntity<List<CommentDto>> getCommentList(Long boardId);
}
