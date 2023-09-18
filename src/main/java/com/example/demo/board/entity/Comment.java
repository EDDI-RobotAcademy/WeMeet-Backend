package com.example.demo.board.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User writer;

    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    @CreationTimestamp
    private LocalDateTime createdDate;
}
