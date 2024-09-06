package com.sparta.maeng9newsfeed.domain.board.dto;

import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentInquiryResponse;
import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.image.entity.Image;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponse {

    private final String userName;
    private final String content;
    private final List<String> imagePathList;
    private final long likes;
    private final List<CommentInquiryResponse> commentList;
    private final LocalDateTime modifiedAt;

    public BoardResponse(Board board) {
        userName = board.getUser().getUserName();
        content = board.getContent();
        imagePathList = board.getImages().stream().map(Image::getImagePath).toList();
        likes = board.getBoardLikeList().size();
        commentList = board.getComments().stream().map(CommentInquiryResponse::commentToDto).toList();
        modifiedAt = board.getModifiedAt();
    }

    public BoardResponse(Board board, List<Image> imageList) {
        userName = board.getUser().getUserName();
        content = board.getContent();
        imagePathList = imageList.stream().map(Image::getImagePath).toList();
        likes = board.getBoardLikeList().size();
        commentList = board.getComments().stream().map(CommentInquiryResponse::commentToDto).toList();
        modifiedAt = board.getModifiedAt();
    }
}
