package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.*;
import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.entity.Comment;
import com.sparta.maeng9newsfeed.repository.BoardRepository;
import com.sparta.maeng9newsfeed.repository.CommentRepository;
import com.sparta.maeng9newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentSaveResponse saveComment(long boardId, CommentSaveRequest commentSaveRequest) {

        Board board = findBoardById(boardId);

        Comment newComment = new Comment(commentSaveRequest.getContent(), board);
        Comment savedComment = commentRepository.save(newComment);

        return CommentSaveResponse.CommentToDto(savedComment);
    }

    @Transactional
    public CommentUpdateResponse updateComment(long boardId, long commentId, CommentUpdateRequest commentUpdateRequest) {

        Comment comment = findCommentdById(commentId);
        comment.update(commentUpdateRequest.getContent());

        return CommentUpdateResponse.CommentToDto(comment);
    }

    public List<CommentInquiryResponse> getComments(long boardId) {

        Board board = findBoardById(boardId);
        List<CommentInquiryResponse> dtoList = new ArrayList<>();

        for (Comment comment : board.getComments()) {
            CommentInquiryResponse commentDto = CommentInquiryResponse.commentToDto(comment);
            dtoList.add(commentDto);
        }
        return dtoList;
    }

    @Transactional
    public void delete(long commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new NullPointerException("존재하지 않는 댓글입니다.");
        }

        commentRepository.deleteById(commentId);
    }

    public Board findBoardById(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("존재하지 않는 피드입니다."));
    }

    public Comment findCommentdById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));
    }
}
