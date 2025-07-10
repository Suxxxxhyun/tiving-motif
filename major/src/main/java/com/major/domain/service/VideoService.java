package com.major.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final WebClient webClient;
    private final Path outputPath = Paths.get("tls/sample");
    private final ReactiveRedisTemplate<String, byte[]> reactiveRedisTemplate;

    public Mono<String> upload(Mono<FilePart> file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        return file
                .filter(part -> Objects.nonNull(part.headers().getContentType()))
                .doOnNext(
                        part -> builder
                                .asyncPart("file", part.content(), DataBuffer.class)
                                .filename(part.filename()))
                .then(Mono.defer(() -> webClient.post() // Mono.defer() -> 매번 새로운 Mono 생성
                        .uri("/api/v1/upload")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .bodyToMono(String.class)));
    }

    public Mono<File> getHlsFile(String filename) {
        return Mono.fromCallable(() -> {
            File file = new File(outputPath + "/" + filename);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + filename);
            }
            return file;
        });
    }

    public Mono<File> getHlsFile(String resolution, String filename) {
        return Mono.fromCallable(() -> {
            File file = new File(outputPath + "/" + resolution + "/" + filename);
            if (!file.exists()) {
                throw new RuntimeException("File not found: " + resolution + "/" + filename);
            }
            return file;
        });
    }

    public Mono<InputStreamResource> getHlsResource(String filename) {
        return getResourceFromCacheOrDisk(filename, outputPath + "/" + filename);
    }

    public Mono<InputStreamResource> getHlsResource(String resolution, String filename) {
        return getResourceFromCacheOrDisk(resolution + "/" + filename, outputPath + "/" + resolution + "/" + filename);
    }

    private Mono<InputStreamResource> getResourceFromCacheOrDisk(String redisKey, String filePath) {
        return reactiveRedisTemplate.opsForValue().get(redisKey)
                .map(bytes -> new InputStreamResource(new ByteArrayInputStream(bytes)))
                .switchIfEmpty(
                        Mono.fromCallable(() -> {
                                    File file = new File(filePath);
                                    if (!file.exists()) {
                                        throw new FileNotFoundException("File not found: " + filePath);
                                    }
                                    return Files.readAllBytes(file.toPath());
                                })
                                .log()
                                .subscribeOn(Schedulers.boundedElastic()) // ✅ 블로킹 I/O 안전하게 실행
                                .log()
                                .flatMap(bytes ->
                                        reactiveRedisTemplate.opsForValue().set(redisKey, bytes)
                                                .thenReturn(new InputStreamResource(new ByteArrayInputStream(bytes)))
                                )
                                .log()
                );
    }
}
