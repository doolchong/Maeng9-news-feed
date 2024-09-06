package com.sparta.maeng9newsfeed.domain.board.repository;

import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByUser_Id(long userId, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.boardLikeList bl WHERE b.createdAt > :createdAt GROUP BY b.id ORDER BY COUNT(bl) DESC")
    Page<Board> findByCreatedAtAfterOrderByBoardLikeCount(@Param("createdAt") LocalDateTime createdAt, Pageable pageable);
}
