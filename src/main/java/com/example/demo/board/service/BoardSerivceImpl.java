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
import java.util.Map;

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
                .content(req.getContents().getContent())
                .build();

        MoimBoard board = MoimBoard.builder()
                .category(BoardCategory.valueOf(req.getCategory().toUpperCase()))
                .title(req.getTitle())
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
                        .build())
                .build();

        return ResponseEntity.ok(boardDto);
    }

    @Override
    public ResponseEntity<List<BoardDto>> getMoimBoardList(Long moimId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<MoimBoard> boardList = boardRepository.findAllMoimBoardWithPageable(moimId, pageable);
        List<BoardDto> responseDtoList = boardList.stream().map((b) ->
                BoardDto.builder()
                        .id(b.getId())
                        .category(b.getCategory().toString())
                        .isPublic(b.getIsPublic())

                        .writer(WriterDto.builder()
                                .id(b.getWriter().getUser().getId())
                                .nickName(b.getWriter().getUser().getNickname())
                                .build())
                        .build()
        ).toList();
        return ResponseEntity.ok(responseDtoList);
    }

    @Override
    @Transactional
    public ResponseEntity<BoardDto> getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        Map<String, Object> addtionalInfo = switch (board.getCategory().toString()) {
            case "MOIM" -> getMoimBoardInfo(board);
            default -> Map.of();
        };

        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .category(board.getCategory().toString())
                .writer(WriterDto.builder()
                        .id(board.getWriter().getUser().getId())
                        .nickName(board.getWriter().getUser().getNickname())
                        .build())
                .contents(BoardContentsDto.builder()
                        .content(board.getContents().getContent())
                        .build())
                .isPublic(board.getIsPublic())
                .title(board.getTitle())
                .build();
        return ResponseEntity.ok(boardDto);
    }

    @Override
    @Transactional
    public ResponseEntity<Map<String, Object>> modifyBoard(Long boardId, BoardDto req) {
        Board board = boardRepository.findById(boardId).get();
        board.getContents().setContent(req.getContents().getContent());
        board.setTitle(req.getTitle());
        boardRepository.save(board);
        return ResponseEntity.ok(Map.of("success", true, "boardId", board.getId()));
    }

    @Override
    public ResponseEntity<Map<String, Object>> postBoard(String category, BoardDto req) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Writer writer = Writer.builder()
                .user(user)
                .build();

        BoardContents contents = BoardContents.builder()
                .content(req.getContents().getContent())
                .build();

        Board board = switch (category) {
            case "qna" ->
                postQnaBoard(req, contents, writer);
            case "faq" -> null;
            default -> null;
        };

        writer.setBoard(board);
        contents.setBoard(board);
        boardRepository.save(board);

        BoardDto boardDto = BoardDto.builder()
                .category(board.getCategory().toString())
                .id(board.getId())
                .build();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "board", boardDto));
    }

    @Override
    public ResponseEntity<List<BoardDto>> getBoardList(String category, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        List<Board> boardList = boardRepository.findAllByCategoryAndPageable(BoardCategory.valueOf(category.toUpperCase()), pageable);
        List<BoardDto> responseList = boardList.stream().map(b->{
            return BoardDto.builder()
                    .id(b.getId())
                    .title(b.getTitle())
                    .writer(WriterDto.builder()
                            .id(b.getWriter().getUser().getId())
                            .nickName(b.getWriter().getUser().getNickname())
                            .build())
                    .build();
        }).toList();
        return ResponseEntity.ok(responseList);
    }

    private Board postQnaBoard(BoardDto req, BoardContents contents, Writer writer) {
        QnaBoard board =  QnaBoard.builder()
                .category(BoardCategory.valueOf(req.getCategory().toUpperCase()))
                .title(req.getTitle())
                .contents(contents)
                .commentList(new ArrayList<>())
                .isPublic(req.getIsPublic())
                .writer(writer)
                .build();
        boardRepository.save(board);
        return board;
    }


    private Map<String, Object> getMoimBoardInfo(Board board) {
        MoimBoard moimBoard = (MoimBoard) board;
        return Map.of("commentList", "commentList");
    }
}
