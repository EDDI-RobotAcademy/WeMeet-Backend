package com.example.demo.board.controller;

import com.example.demo.board.controller.dto.BoardDto;
import com.example.demo.board.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long boardId) {
        return boardSerivce.getBoard(boardId);
    }

    @PutMapping(value = "/{boardId}")
    public ResponseEntity<Map<String, Object>> modifyBoard(@PathVariable Long boardId, @RequestBody BoardDto req) {
        return boardSerivce.modifyBoard(boardId, req);
    }
    @PostMapping("/{category}")
    public ResponseEntity<Map<String, Object>> postBoard(@PathVariable String category, @RequestBody BoardDto req) {
        return boardSerivce.postBoard(category, req);
    }
    @GetMapping(value = "/list/{category}", params = {"page", "size"})
    public ResponseEntity<List<BoardDto>> getBoardList(@PathVariable String category, @RequestParam Integer page, @RequestParam Integer size) {
        return boardSerivce.getBoardList(category, page, size);
    }
    @DeleteMapping(value="/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable Long boardId) {
        return boardSerivce.deleteBoard(boardId);
    }
}
