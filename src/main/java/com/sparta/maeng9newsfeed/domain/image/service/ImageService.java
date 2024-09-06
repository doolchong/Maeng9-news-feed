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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    
    @Transactional
    public List<Image> uploadImage(Board board, List<MultipartFile> images) {
        String uploadDirectory = "src/main/resources/static/images/";

        return images.stream().map(image -> {
            try {
                String dbFilePath = saveImage(image, uploadDirectory);
                Image img = new Image(dbFilePath, board);
                imageRepository.save(img);
                return img;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("파일 저장 중 오류 발생", e);
            }
        }).toList();
    }

    private String saveImage(MultipartFile image, String uploadDirectory) throws IOException {
        String fileName = UUID.randomUUID().toString().replace("-", "") + "-" + image.getOriginalFilename();    //파일 이름 UUID를 이용해 랜덤으로 생성
        String filePath = uploadDirectory + fileName;   //실제 파일이 저장되는 경로
        String dbImagePath = "/images/" + fileName; //DB에 저장할 경로 문자열

        Path path = Paths.get(filePath);    //Path 객체 생성
        Files.createDirectories(path.getParent());  //디렉토리 생성
        Files.write(path, image.getBytes());    //디렉토리에 파일 저장

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
