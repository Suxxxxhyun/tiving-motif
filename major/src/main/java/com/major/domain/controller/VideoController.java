package com.major.domain.controller;

import com.major.domain.dto.MyMessage;
import com.major.domain.producer.MyProducer;
import com.major.domain.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final MyProducer myProducer;

    @PostMapping("/upload")
    public Mono<MyMessage> upload(
            @RequestPart("file") Mono<FilePart> file
    ) throws IOException {
        return videoService.upload(file).map(MyMessage::of);
    }

    @PostMapping("/convert")
    public void convert(@RequestBody MyMessage message){
        myProducer.sendMessage(message);
    }
}
