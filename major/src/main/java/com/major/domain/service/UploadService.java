package com.major.domain.service;

import com.major.domain.dto.Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final WebClient webClient;
    public Mono<Upload.Response> upload(MultipartFile file) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/upload")
                        .queryParam("file", file)
                        .build())
                .retrieve()
                .bodyToMono(Upload.Response.class);
    }
}
