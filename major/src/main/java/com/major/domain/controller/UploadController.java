package com.major.domain.controller;

import com.major.domain.dto.Upload;
import com.major.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<Mono<Upload.Response>> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var reponse = uploadService.upload(file);
        return ResponseEntity.ok(reponse);
    }
}
