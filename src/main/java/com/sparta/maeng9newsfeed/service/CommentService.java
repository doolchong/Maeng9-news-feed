package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.*;
import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.entity.Comment;
import com.sparta.maeng9newsfeed.entity.User;
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
    public CommentSaveResponse saveComment(long userId, long boardId, CommentSaveRequest commentSaveRequest) {

        Board board = findBoardById(boardId);
        User user = findUserById(userId);

        Comment newComment = new Comment(commentSaveRequest.getContent(), board, user);
        Comment savedComment = commentRepository.save(newComment);

        return CommentSaveResponse.CommentToDto(savedComment);
    }

    @Transactional
    public CommentUpdateResponse updateComment(long userId, long boardId, long commentId, CommentUpdateRequest commentUpdateRequest) {

        Board board = findBoardById(boardId);
        Comment comment = findCommentdById(commentId);

        if (userId == comment.getUser().getId() ||
                userId == board.getUser().getId()) {
            comment.update(commentUpdateRequest.getContent());
        } else {
            throw new IllegalArgumentException("게시글의 작성자 또는 댓글 작성자만 수정할 수 있습니다.");
        }

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
    public void delete(long userId, long boardId, long commentId) {

        Board board = findBoardById(boardId);
        Comment comment = findCommentdById(commentId);

        if (userId == comment.getUser().getId() ||
                userId == board.getUser().getId()) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException("게시글의 작성자 또는 댓글 작성자만 삭제할 수 있습니다.");
        }

    }

    public Board findBoardById(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("존재하지 않는 피드입니다."));
    }

    public Comment findCommentdById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));
    }

    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));
    }
}
