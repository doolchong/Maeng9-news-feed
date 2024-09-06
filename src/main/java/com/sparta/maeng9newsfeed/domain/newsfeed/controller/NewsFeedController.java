package com.sparta.maeng9newsfeed.domain.newsfeed.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.common.dto.AuthUser;
import com.sparta.maeng9newsfeed.domain.board.dto.BoardResponse;
import com.sparta.maeng9newsfeed.domain.newsfeed.service.NewsFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @GetMapping("/newsfeed")
    public ResponseEntity<List<BoardResponse>> getNewsFeed(@Auth AuthUser authUser) {
        return ResponseEntity.ok().body(newsFeedService.getNewsFeed(authUser.getId()));
    }
}
