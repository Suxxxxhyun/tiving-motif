package com.major.domain.controller;

import com.major.domain.dto.MyMessage;
import com.major.domain.producer.MyProducer;
import com.major.domain.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final MyProducer myProducer;

    @PostMapping("/upload")
    public Mono<MyMessage> upload(@RequestPart("file") Mono<FilePart> file) throws IOException {
        return videoService.upload(file).map(MyMessage::of);
    }

    @PostMapping("/convert")
    public void convert(@RequestBody MyMessage message) {
        myProducer.sendMessage(message);
    }

    @ResponseBody
    @RequestMapping("/hls/{filename}")
    public Mono<InputStreamResource> getMaster(
            @PathVariable String filename) {
        return videoService.getHlsFile(filename)
                .<InputStreamResource>handle((file, sink) -> {
                    try {
                        sink.next(new InputStreamResource(new FileInputStream(file)));
                    } catch (FileNotFoundException e) {
                        sink.error(new RuntimeException("File not found: " + filename, e));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error serving HLS file: {}", filename, e);
                    return Mono.empty();
                });
    }

    // 각 화질별 ts, m3u8 경로
    @ResponseBody
    @RequestMapping("/hls/{resolution}/{filename}")
    public Mono<InputStreamResource> getPlaylist(
            @PathVariable String resolution,
            @PathVariable String filename) {
        return videoService.getHlsFile(resolution, filename)
                .<InputStreamResource>handle((file, sink) -> {
                    try {
                        sink.next(new InputStreamResource(new FileInputStream(file)));
                    } catch (FileNotFoundException e) {
                        sink.error(new RuntimeException("File not found: " + resolution + "/" + filename, e));
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error serving HLS file: {}/{}", resolution, filename, e);
                    return Mono.empty();
                });
    }
}
