package com.sparta.maeng9newsfeed.controller;

import com.sparta.maeng9newsfeed.dto.BoardResponse;
import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<BoardResponse> create(@RequestPart("content") String content, @RequestPart("image") List<MultipartFile> images) {
        return ResponseEntity.ok().body(boardService.create(content, images));
    }

    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable long boardId) {
        return ResponseEntity.ok().body(boardService.getBoard(boardId));
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> update(@PathVariable long boardId, @RequestPart("content") String content, @RequestPart("image") List<MultipartFile> images) {
        return ResponseEntity.ok().body(boardService.getBoard(boardService.update(boardId, content, images)));
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<String> delete(@PathVariable long boardId) {
        return ResponseEntity.ok().body(boardService.delete(boardId));
    }
}
