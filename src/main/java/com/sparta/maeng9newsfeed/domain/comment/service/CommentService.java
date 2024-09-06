package com.sparta.maeng9newsfeed.domain.comment.service;

import com.sparta.maeng9newsfeed.domain.comment.dto.request.CommentSaveRequest;
import com.sparta.maeng9newsfeed.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentInquiryResponse;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentSaveResponse;
import com.sparta.maeng9newsfeed.domain.comment.dto.response.CommentUpdateResponse;
import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.comment.entity.Comment;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import com.sparta.maeng9newsfeed.domain.board.repository.BoardRepository;
import com.sparta.maeng9newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.maeng9newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 저장
     *
     * @param userId             현재 로그인 된 사용자의 ID
     * @param boardId            현지 게시글의 ID
     * @param commentSaveRequest 작성한 댓글 내용을 담은 DTO
     * @return 성공 시 댓글 내용을 담은 DTO
     */
    @Transactional
    public CommentSaveResponse saveComment(long userId, long boardId, CommentSaveRequest commentSaveRequest) {
        Board board = findBoardById(boardId);
        User user = findUserById(userId);

        Comment newComment = new Comment(commentSaveRequest.getContent(), board, user);
        Comment savedComment = commentRepository.save(newComment);
        return CommentSaveResponse.CommentToDto(savedComment);
    }

    /**
     * 댓글 수정
     *
     * @param userId               현재 로그인 된 사용자의 ID
     * @param boardId              현재 게시글의 ID
     * @param commentId            수정할 댓글의 ID
     * @param commentUpdateRequest 수정할 댓글 내용을 담은 DTO
     * @return 성공 시 수정된 댓글 내용을 담은 DTO
     */
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

    /**
     * 특정 게시글의 댓글 목록 조회
     *
     * @param boardId 특정 게시글의 ID
     * @return List<CommentInquiryResponse> : 요청한 게시글의 전체 댓글 목록
     */
    public List<CommentInquiryResponse> getComments(long boardId) {
        return findBoardById(boardId).getComments().stream()
                .map(CommentInquiryResponse::commentToDto)
                .toList();
    }

    /**
     * 댓글 삭제
     *
     * @param userId    현재 로그인 된 사용자의 ID
     * @param boardId
     * @param commentId
     */
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
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 피드입니다."));
    }

    public Comment findCommentdById(long commentId) {
        return commentRepository
                .findById(commentId).orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));
    }

    public User findUserById(long userId) {
        return userRepository
                .findById(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 유저입니다."));
    }
}
