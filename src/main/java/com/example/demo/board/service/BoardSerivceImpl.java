package com.example.demo.board.service;

import com.example.demo.board.controller.dto.BoardContentsDto;
import com.example.demo.board.controller.dto.BoardDto;
import com.example.demo.board.controller.dto.WriterDto;
import com.example.demo.board.entity.*;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.moim.repository.MoimRepository;
import com.example.demo.security.costomUser.CustomUserDetails;
import com.example.demo.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardSerivceImpl implements BoardSerivce {
    final BoardRepository boardRepository;
    final MoimRepository moimRepository;
    @Override
    @Transactional
    public ResponseEntity<BoardDto> post(Long moimId, BoardDto req) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Writer writer = Writer.builder()
                .user(user)
                .build();

        BoardContents contents = BoardContents.builder()
                .title(req.getContents().getTitle())
                .content(req.getContents().getContent())
                .build();

        MoimBoard board = MoimBoard.builder()
                .category(BoardCategory.valueOf(req.getCategory()))
                .contents(contents)
                .commentList(new ArrayList<>())
                .isPublic(req.getIsPublic())
                .moim(moimRepository.findById(moimId).get())
                .writer(writer)
                .build();

        writer.setBoard(board);
        contents.setBoard(board);
        boardRepository.save(board);

        BoardDto boardDto = BoardDto.builder()
                .category(board.getCategory().toString())
                .id(board.getId())
                .contents(BoardContentsDto.builder()
                        .content(board.getContents().getContent())
                        .title(board.getContents().getTitle())
                        .build())
                .build();

        return ResponseEntity.ok(boardDto);
    }

    @Override
    public ResponseEntity<List<BoardDto>> getMoimBoardList(Long moimId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<MoimBoard> boardList =boardRepository.findAllMoimBoardWithPageable(moimId, pageable);
        List<BoardDto> responseDtoList = boardList.stream().map((b)->
                BoardDto.builder()
                        .id(b.getId())
                        .isPublic(b.getIsPublic())
                        .contents(
                                BoardContentsDto.builder()
                                        .title(b.getContents().getTitle())
                                        .build()
                        )
                        .writer(WriterDto.builder()
                                .id(b.getWriter().getUser().getId())
                                .nickName(b.getWriter().getUser().getNickname())
                                .build())
                        .build()
                ).toList();
        return ResponseEntity.ok(responseDtoList);
    }
}
