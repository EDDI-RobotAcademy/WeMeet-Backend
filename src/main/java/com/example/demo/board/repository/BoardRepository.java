package com.example.demo.board.repository;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.MoimBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select mb from MoimBoard mb join fetch mb.writer.user join fetch mb.contents where mb.moim.id=:moimId")
    List<MoimBoard> findAllMoimBoardWithPageable(Long moimId, Pageable pageable);
}
