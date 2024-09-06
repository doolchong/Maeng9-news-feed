package com.sparta.maeng9newsfeed.domain.like.comment.service;

import com.sparta.maeng9newsfeed.annotation.Auth;
import com.sparta.maeng9newsfeed.domain.comment.entity.Comment;
import com.sparta.maeng9newsfeed.domain.like.comment.entity.CommentLike;
import com.sparta.maeng9newsfeed.domain.user.entity.User;
import com.sparta.maeng9newsfeed.domain.like.comment.repository.CommentLikeRepository;
import com.sparta.maeng9newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.maeng9newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /**
     * @param userId    현재 로그인 된 사용자의 ID
     * @param commentId 해당 댓글의 ID
     * @return 좋아요를 누른 적 없었을 "좋아요", 좋아요 누른 적 있을 시 "좋아요 취소"
     */
    @Transactional
    public String likeComment(long userId, long commentId) {
        if (findCommentById(userId).getUser().getId() == userId) {
            throw new IllegalArgumentException("자신의 댓글에 좋아요를 할 수 없습니다.");
        }

        if (!commentLikeRepository.findByComment_IdAndUser_Id(commentId, userId).isPresent()) {

            Comment comment = findCommentById(commentId);
            User user = findUserById(userId);

            CommentLike newCommentLike = new CommentLike(comment, user);
            commentLikeRepository.save(newCommentLike);
            return "좋아요";
        } else {
            commentLikeRepository.deleteByComment_IdAndUser_Id(commentId, userId);
            return "좋아요 취소";
        }
    }

    public Comment findCommentById(long boardId) {
        return commentRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 피드입니다."));
    }

    public User findUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));
    }
}
