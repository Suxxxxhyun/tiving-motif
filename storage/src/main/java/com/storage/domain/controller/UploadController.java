package com.storage.domain.controller;

import com.storage.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public Mono<String> upload(
            @RequestPart("file") Mono<FilePart> file
    ) throws IOException {
        return uploadService.upload(file);
    }
}
