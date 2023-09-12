package com.example.demo.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue(value="qna")
public class QnaBoard extends Board{

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "board")
    private List<Comment> commentList;
}
