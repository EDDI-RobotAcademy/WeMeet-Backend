package com.example.demo.board.controller;

import com.example.demo.board.controller.dto.CommentDto;
import com.example.demo.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    final CommentService commentService;
    @GetMapping(value = "/list/board/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable Long boardId) {
        return commentService.getCommentList(boardId);
    }

    @PostMapping("/board/{boardId}")
    public ResponseEntity<Map<String, Object>> postComment(@PathVariable Long boardId, @RequestBody CommentDto req) {
        log.info("postComment");
        return commentService.postComment(boardId, req);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

}
