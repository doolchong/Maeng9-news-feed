package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.dto.AuthUser;
import com.sparta.maeng9newsfeed.dto.BoardResponse;
import com.sparta.maeng9newsfeed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardResponse> create(@RequestPart("content") String content, @RequestPart("image") List<MultipartFile> images, @Auth AuthUser authUser) {
        return ResponseEntity.ok().body(boardService.create(content, images, authUser.getId()));
    }

    @GetMapping("/boards")
    public ResponseEntity<Page<BoardResponse>> getMyBoardList(@Auth AuthUser authUser) {
        return ResponseEntity.ok().body(boardService.getMyBoardList(authUser.getId()));
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> update(@PathVariable long boardId, @RequestPart("content") String content, @RequestPart("image") List<MultipartFile> images, @Auth AuthUser authUser) {
        return ResponseEntity.ok().body(boardService.getBoard(boardService.update(boardId, content, images, authUser.getId())));
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<String> delete(@PathVariable long boardId, @Auth AuthUser authUser) {
        return ResponseEntity.ok().body(boardService.delete(boardId, authUser.getId()));
    }

    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<String> boardLike(@PathVariable long boardId, @Auth AuthUser authUser) {
        return ResponseEntity.ok().body(boardService.boardLike(boardId, authUser.getId()));
    }

    @GetMapping("/boards/hot")
    public ResponseEntity<Page<BoardResponse>> getHotBoardList() {
        return ResponseEntity.ok().body(boardService.getHotBoardList());
    }
}
