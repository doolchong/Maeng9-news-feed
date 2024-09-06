package com.sparta.maeng9newsfeed.domain.image.repository;

import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteAllByBoard(Board board);
}
