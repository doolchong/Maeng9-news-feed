package com.sparta.maeng9newsfeed.domain.like.comment.repository;

import com.sparta.maeng9newsfeed.domain.like.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByComment_IdAndUser_Id(long commentId, long userId);

    void deleteByComment_IdAndUser_Id(long commentId, long userId);
}
