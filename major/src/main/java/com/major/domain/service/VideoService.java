package com.major.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final WebClient webClient;
    private final Path outputPath = Paths.get("tls/sample");

    public Mono<String> upload(Mono<FilePart> file) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        return file
                .filter(part -> Objects.nonNull(part.headers().getContentType()))
                .doOnNext(
                        part -> builder
                                .asyncPart("file", part.content(), DataBuffer.class)
                                .filename(part.filename())
                )
                .then(Mono.defer(() -> webClient.post()
                        .uri("/api/v1/upload")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .bodyToMono(String.class)
                ));
    }

    public File getHlsFile(String filename) {
        return new File(outputPath + "/" + filename);
    }

    public File getHlsFile(String resolution, String filename) {
        return new File(outputPath + "/" + resolution + "/" + filename);
    }
}
