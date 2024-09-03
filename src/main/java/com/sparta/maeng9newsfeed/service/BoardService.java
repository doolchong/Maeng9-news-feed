package com.sparta.maeng9newsfeed.service;

import com.sparta.maeng9newsfeed.dto.BoardResponse;
import com.sparta.maeng9newsfeed.entity.Board;
import com.sparta.maeng9newsfeed.entity.Image;
import com.sparta.maeng9newsfeed.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ImageService imageService;

    @Transactional
    public BoardResponse create(String content, List<MultipartFile> images) {
        Board board = new Board(content);

        Board saveBoard = boardRepository.save(board);

        List<Image> imageList = imageService.uploadImage(saveBoard, images);

        return new BoardResponse(saveBoard, imageList);
    }

    public BoardResponse getBoard(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));

        return new BoardResponse(board);
    }

    @Transactional
    public String delete(long boardId) {
        boardRepository.delete(findBoardById(boardId));
        return "게시글 삭제 완료";
    }

    private Board findBoardById(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );
    }

    @Transactional
    public long update(long boardId, String content, List<MultipartFile> images) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물 입니다.")
        );
        board.updateBoard(content);
        imageService.updateImage(board, images);

        return boardId;
    }
}
