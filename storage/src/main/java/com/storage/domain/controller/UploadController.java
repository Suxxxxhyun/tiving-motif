package com.storage.domain.controller;

import com.storage.domain.dto.Upload;
import com.storage.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private UploadService uploadService;

    @PostMapping
    public ResponseEntity<Upload.Response> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var reponse = uploadService.upload(file);
        return ResponseEntity.ok(reponse);
    }
}
