package com.sparta.maeng9newsfeed.domain.like.comment.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.common.dto.AuthUser;
import com.sparta.maeng9newsfeed.domain.like.comment.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/boards/{boardId}/{commentId}/like")
    public void likeComment(@Auth AuthUser authUser, @PathVariable long commentId) {
        commentLikeService.likeComment(authUser.getId(), commentId);
    }
}
