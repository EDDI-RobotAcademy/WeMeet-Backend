package com.example.demo.board.service;

import com.example.demo.board.controller.dto.CommentDto;
import com.example.demo.board.controller.dto.WriterDto;
import com.example.demo.board.entity.Comment;
import com.example.demo.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;

    @Override
    public ResponseEntity<List<CommentDto>> getCommentList(Long boardId) {
        List<Comment> commentList = commentRepository.findAllByBoardId(boardId);
        List<CommentDto> responseList = commentList.stream().map((c)->
                CommentDto.builder()
                        .id(c.getId())
                        .writer(WriterDto.builder()
                                .id(c.getWriter().getUser().getId())
                                .nickName(c.getWriter().getUser().getNickname())
                                .build())
                        .contents(c.getContents())
                        .createdTime(c.getCreatedDate())
                        .build()
                ).toList();
        return ResponseEntity.ok(responseList);
    }
}
