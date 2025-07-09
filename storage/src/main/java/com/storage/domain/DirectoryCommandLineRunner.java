package com.storage.domain;

import com.storage.domain.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectoryCommandLineRunner implements CommandLineRunner {

    private final UploadService uploadService;

    @Override
    public void run(String... arg) throws Exception {
        uploadService.init();
    }
}
