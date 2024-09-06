package com.sparta.maeng9newsfeed.domain.comment.controller;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.common.dto.AuthUser;
import com.sparta.maeng9newsfeed.domain.comment.dto.request.CommentSaveRequest;
import com.sparta.maeng9newsfeed.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentInquiryResponse;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentSaveResponse;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentUpdateResponse;
import com.sparta.maeng9newsfeed.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<CommentSaveResponse> saveComment(@Auth AuthUser authUser,
                                                           @PathVariable long boardId,
                                                           @RequestBody CommentSaveRequest commentSaveRequest) {
        return ResponseEntity.ok(commentService.saveComment(authUser.getId(), boardId, commentSaveRequest));
    }

    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(@Auth AuthUser authUser,
                                                               @PathVariable long boardId,
                                                               @PathVariable long commentId,
                                                               @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return ResponseEntity.ok(commentService.updateComment(authUser.getId(), boardId, commentId, commentUpdateRequest));
    }

    @GetMapping("/{boardId}/comment")
    public ResponseEntity<List<CommentInquiryResponse>> getComment(@PathVariable long boardId) {
        return ResponseEntity.ok(commentService.getComments(boardId));
    }

    @DeleteMapping("/{boardId}/{commentId}")
    public void deleteComment(@Auth AuthUser authUser,
                              @PathVariable long boardId,
                              @PathVariable long commentId) {
        commentService.delete(authUser.getId(), boardId, commentId);
    }
}
