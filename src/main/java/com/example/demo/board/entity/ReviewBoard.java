package com.example.demo.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue(value="review")
public class ReviewBoard extends Board{

}
