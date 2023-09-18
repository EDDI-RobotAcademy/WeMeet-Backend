package com.example.demo.board.service;

import com.example.demo.board.controller.dto.CommentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CommentService {
    ResponseEntity<List<CommentDto>> getCommentList(Long boardId);

    ResponseEntity<Map<String, Object>> postComment(Long boardId, CommentDto req);

    ResponseEntity<Map<String, Object>> deleteComment(Long commentId);
}
