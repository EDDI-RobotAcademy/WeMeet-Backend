package com.example.demo.board.repository;

import com.example.demo.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.writer.user where c.board.id = :boardId")
    List<Comment> findAllByBoardId(Long boardId);
}
