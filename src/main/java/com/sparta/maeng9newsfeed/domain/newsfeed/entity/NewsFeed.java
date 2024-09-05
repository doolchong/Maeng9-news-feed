package com.sparta.maeng9newsfeed.domain.newsfeed.entity;

import com.sparta.maeng9newsfeed.domain.board.entity.Board;
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
