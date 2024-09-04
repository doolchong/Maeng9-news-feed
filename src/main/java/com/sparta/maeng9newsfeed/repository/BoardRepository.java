package com.sparta.maeng9newsfeed.repository;

import com.sparta.maeng9newsfeed.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByUser_Id(long userId, Pageable pageable);
}
