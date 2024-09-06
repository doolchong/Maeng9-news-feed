package com.sparta.maeng9newsfeed.domain.board.service;

import com.sparta.maeng9newsfeed.domain.board.dto.BoardResponse;
import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.like.board.entity.BoardLike;
import com.sparta.maeng9newsfeed.domain.image.entity.Image;
import com.sparta.maeng9newsfeed.domain.like.board.repository.BoardLikeRepository;
import com.sparta.maeng9newsfeed.domain.board.repository.BoardRepository;
import com.sparta.maeng9newsfeed.domain.image.service.ImageService;
import com.sparta.maeng9newsfeed.domain.newsfeed.service.NewsFeedService;
import com.sparta.maeng9newsfeed.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;
    private final BoardLikeRepository boardLikeRepository;
    private final UserService userService;
    private final NewsFeedService newsFeedService;

    @Transactional
    public BoardResponse create(String content, List<MultipartFile> images, long userId) {
        Board board = new Board(content, userService.findByUserId(userId));

        Board saveBoard = boardRepository.save(board);

        List<Image> imageList = imageService.uploadImage(saveBoard, images);

        newsFeedService.create(saveBoard);

        return new BoardResponse(saveBoard, imageList);
    }

    public BoardResponse getBoard(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));

        return new BoardResponse(board);
    }

    public Page<BoardResponse> getMyBoardList(long userId) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("modifiedAt").descending());

        Page<Board> boardList = boardRepository.findAllByUser_Id(userId, pageable);

        return boardList.map(BoardResponse::new);
    }

    @Transactional
    public String delete(long boardId, long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );
        if (board.getUser().getId() == userId) {
            boardRepository.delete(findBoardById(boardId));
        } else {
            throw new IllegalArgumentException("게시글을 작성한 사용자가 아닙니다.");
        }

        return "게시글 삭제 완료";
    }

    public Board findBoardById(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
    }

    @Transactional
    public long update(long boardId, String content, List<MultipartFile> images, long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );
        if (board.getUser().getId() == userId) {
            board.updateBoard(content);
            imageService.updateImage(board, images);
        } else {
            throw new IllegalArgumentException("게시글을 작성한 사용자가 아닙니다.");
        }

        return boardId;
    }

    @Transactional
    public String boardLike(long boardId, long userId) {
        if (findBoardById(boardId).getUser().getId() == userId) {
            throw new IllegalArgumentException("자신의 게시물에 좋아요를 할 수 없습니다.");
        }
        if (boardLikeRepository.findByBoard_IdAndUser_Id(boardId, userId).isPresent()) {
            boardLikeRepository.deleteByBoard_IdAndUser_Id(boardId, userId);
            return "좋아요를 취소했습니다.";
        } else {
            BoardLike boardLike = new BoardLike(findBoardById(boardId), userService.findByUserId(userId));
            boardLikeRepository.save(boardLike);
            return "좋아요를 했습니다.";
        }
    }

    public Page<BoardResponse> getHotBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(1); // 1일을 기준
        Page<Board> hotBoardList = boardRepository.findByCreatedAtAfterOrderByBoardLikeCount(expirationDate, pageable);
        return hotBoardList.map(BoardResponse::new);
    }
}
