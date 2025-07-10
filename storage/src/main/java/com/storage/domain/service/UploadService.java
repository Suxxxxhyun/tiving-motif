package com.storage.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final Path root = Paths.get("uploads");

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Mono<String> upload(Mono<FilePart> filePartMono){
        return filePartMono.doOnNext(
                fp -> System.out.println("Receiving File:" + fp.filename()))
                .flatMap(filePart -> {
            String filename = filePart.filename();
            return filePart.transferTo(root.resolve(filename)).then(Mono.just(filename));
        });
    }
}
