package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentInquiryResponse {

    private final String userName;
    private final String content;
    private final LocalDateTime modifiedAt;
    private final long commentLikes;

    public static CommentInquiryResponse commentToDto(Comment comment) {
        return new CommentInquiryResponse(
                comment.getUser().getUserName(),
                comment.getContent(),
                comment.getModifiedAt(),
                comment.getCommentLikes().stream().count()
        );
    }
}
