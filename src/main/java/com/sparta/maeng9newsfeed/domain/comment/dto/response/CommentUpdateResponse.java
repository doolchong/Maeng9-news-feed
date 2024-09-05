package com.sparta.maeng9newsfeed.domain.comment.dto.response;

import com.sparta.maeng9newsfeed.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentUpdateResponse {

    private final String content;
    private final LocalDateTime modifiedAt;

    public static CommentUpdateResponse CommentToDto(Comment comment) {

        return new CommentUpdateResponse(
                comment.getContent(),
                comment.getModifiedAt()
        );
    }
}
