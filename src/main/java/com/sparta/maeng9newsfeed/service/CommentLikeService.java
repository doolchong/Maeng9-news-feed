package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.entity.Comment;
import com.sparta.maeng9newsfeed.entity.CommentLike;
import com.sparta.maeng9newsfeed.entity.User;
import com.sparta.maeng9newsfeed.repository.CommentLikeRepository;
import com.sparta.maeng9newsfeed.repository.CommentRepository;
import com.sparta.maeng9newsfeed.repository.UserRepository;
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


    @Transactional
    public String likeComment(long commentId, long userId) {

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
        return commentRepository.findById(boardId).orElseThrow(() -> new NullPointerException("존재하지 않는 피드입니다."));
    }

    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));
    }
}
