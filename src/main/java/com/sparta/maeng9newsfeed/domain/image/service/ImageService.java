package com.sparta.maeng9newsfeed.domain.image.service;

import com.sparta.maeng9newsfeed.domain.board.entity.Board;
import com.sparta.maeng9newsfeed.domain.image.entity.Image;
import com.sparta.maeng9newsfeed.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public List<Image> uploadImage(Board board, List<MultipartFile> images) {
        List<Image> imageList = new ArrayList<>();

        try {
            String uploadDirectory = "src/main/resources/static/images/";

            for (MultipartFile image : images) {
                //이미지 파일 경로를 저장
                String dbFilePath = saveImage(image, uploadDirectory);

                Image img = new Image(dbFilePath, board);

                imageRepository.save(img);

                imageList.add(img);
            }
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 처리
            e.printStackTrace();
        }

        return imageList;
    }

    private String saveImage(MultipartFile image, String uploadDirectory) throws IOException {
        //파일 이름 UUID를 이용해 랜덤으로 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "-" + image.getOriginalFilename();

        //실제 파일이 저장되는 경로
        String filePath = uploadDirectory + fileName;

        //DB에 저장할 경로 문자열
        String dbImagePath = "/images/" + fileName;

        //Path 객체 생성
        Path path = Paths.get(filePath);
        //디렉토리 생성
        Files.createDirectories(path.getParent());
        //디렉토리에 파일 저장
        Files.write(path, image.getBytes());

        return dbImagePath;
    }

    public void updateImage(Board board, List<MultipartFile> images) {
        deleteImage(board);
        uploadImage(board, images);
    }

    @Transactional
    public void deleteImage(Board board) {
        imageRepository.deleteAllByBoard(board);
    }
}
