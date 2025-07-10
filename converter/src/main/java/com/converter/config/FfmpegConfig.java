package com.converter.config;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FfmpegConfig {
    @Value("${ffmpeg.location}") // 설정 파일에서 ffmpeg 경로를 가져옴
    private String ffmpegPath;

    @Value("${ffprobe.location}") // 설정 파일에서 ffprobe 경로를 가져옴
    private String ffprobePath;

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        return new FFmpeg(ffmpegPath);
    }

    @Bean
    public FFprobe ffprobe() throws IOException {
        return new FFprobe(ffprobePath);
    }
}
