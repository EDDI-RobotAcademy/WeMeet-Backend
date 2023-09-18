package com.example.demo.board.service;

import com.example.demo.board.controller.dto.CommentDto;
import com.example.demo.board.controller.dto.WriterDto;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.Comment;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.board.repository.CommentRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;
    final BoardRepository boardRepository;

    @Override
    public ResponseEntity<List<CommentDto>> getCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId);
        List<CommentDto> responseList = commentList.stream().map((c)->
                CommentDto.builder()
                        .id(c.getId())
                        .writer(WriterDto.builder()
                                .id(c.getWriter().getId())
                                .nickName(c.getWriter().getNickname())
                                .build())
                        .contents(c.getContents())
                        .createdTime(c.getCreatedDate())
                        .build()
                ).toList();
        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<Map<String, Object>> postComment(Long boardId, CommentDto req) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Comment comment = Comment.builder()
                .board((Board)boardRepository.findById(boardId).get())
                .contents(req.getContents())
                .writer(user)
                .build();
        commentRepository.save(comment);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.delete(comment);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
