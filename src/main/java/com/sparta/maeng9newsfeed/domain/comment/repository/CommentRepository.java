package com.sparta.maeng9newsfeed.domain.comment.repository;

import com.sparta.maeng9newsfeed.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
