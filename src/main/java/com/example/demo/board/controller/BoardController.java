package com.example.demo.board.controller;

import com.example.demo.board.controller.dto.BoardDto;
import com.example.demo.board.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    final BoardSerivce boardSerivce;
    @PostMapping("/moim/{moimId}")
    public ResponseEntity<BoardDto> postMoimBoard(@PathVariable Long moimId, @RequestBody BoardDto req) {
        return boardSerivce.post(moimId, req);
    }
    @GetMapping(value = "/list/moim/{moimId}", params = {"page", "size"})
    public ResponseEntity<List<BoardDto>> getMoimBoardList(@PathVariable Long moimId, @RequestParam Integer page, @RequestParam Integer size) {
        return boardSerivce.getMoimBoardList(moimId, page, size);
    }
    @GetMapping(value = "/{category}/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long boardId, @PathVariable String category) {
        return boardSerivce.getBoard(boardId, category);
    }
}
