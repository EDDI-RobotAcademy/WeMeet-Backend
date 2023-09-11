package com.example.demo.board.controller;

import com.example.demo.board.controller.dto.BoardDto;
import com.example.demo.board.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    final BoardSerivce boardSerivce;
    @PostMapping("/moim/{moimId}")
    public ResponseEntity<BoardDto> postMoimBoard(@PathVariable Long moimId, @RequestBody BoardDto req) {
        return boardSerivce.post(moimId, req);
    }
}
