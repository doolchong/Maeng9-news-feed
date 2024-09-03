package com.sparta.maeng9newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public Image(String imagePath, Board board) {
        this.imagePath = imagePath;
        this.board = board;
    }
}
