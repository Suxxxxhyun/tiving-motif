package com.storage.domain.service;

import com.storage.domain.dto.Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.hls.dir}")
    private String hlsDir;

    public Upload.Response upload(MultipartFile file) throws IOException{
        createDirectories();

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        log.info("originalFileName : {}", originalFileName);
        log.info("fileExtension : {}", fileExtension);
        log.info("uniqueFileName : {}", uniqueFileName);

        Path uploadPath = Paths.get(uploadDir, uniqueFileName);
        Files.copy(file.getInputStream(), uploadPath);

        return Upload.Response.of(originalFileName, fileExtension, uniqueFileName);
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
        Files.createDirectories(Paths.get(hlsDir));
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
