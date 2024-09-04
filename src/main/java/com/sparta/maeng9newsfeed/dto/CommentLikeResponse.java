package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentLikeResponse {

    private final String userName;

    public static CommentLikeResponse CommentLikeToDto(User user) {
        return new CommentLikeResponse(user.getUserName());
    }
}
