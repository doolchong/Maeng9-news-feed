package com.sparta.maeng9newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class NewsFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_feed_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public NewsFeed(Board board) {
        this.board = board;
    }
}
