package com.sparta.maeng9newsfeed.dto;

import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.entity.Image;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardResponse {

    private final String content;
    private final List<String> imagePathList;

    public BoardResponse(Board board) {
        content = board.getContent();
        imagePathList = board.getImages().stream().map(Image::getImagePath).toList();
    }

    public BoardResponse(Board board, List<Image> imageList) {
        content = board.getContent();
        imagePathList = imageList.stream().map(Image::getImagePath).toList();
    }
}
