package com.sparta.maeng9newsfeed.repository;

import com.sparta.maeng9newsfeed.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Optional<BoardLike> findByBoard_IdAndUser_Id(long boardId, long userId);

    void deleteByBoard_IdAndUser_Id(long boardId, long userId);
}
