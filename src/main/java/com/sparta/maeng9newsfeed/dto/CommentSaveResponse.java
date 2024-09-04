package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentSaveResponse {

    private final String user;
    private final String content;

    public static CommentSaveResponse CommentToDto(Comment comment) {
        return new CommentSaveResponse(
                comment.getUser().getUserName(),
                comment.getContent()
        );
    }
}
