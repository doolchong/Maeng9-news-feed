package com.sparta.maeng9newsfeed.repository;

import com.sparta.maeng9newsfeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
