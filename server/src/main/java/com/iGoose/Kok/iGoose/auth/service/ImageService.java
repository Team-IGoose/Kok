package com.iGoose.Kok.iGoose.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {
    private final String uploadDir = "C:/Users/tp519/Documents/GitHub/Kok/server/src/main/resources/images/";

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "http://localhost:8080/images/" + fileName;
        return imageUrl;
    }
}
