package com.sparta.maeng9newsfeed.domain.board.repository;

import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByUser_Id(long userId, Pageable pageable);

    Page<Board> findAllByCreatedAtAfterOrderByCommentsDesc(LocalDateTime expirationDate, Pageable pageable);
}
