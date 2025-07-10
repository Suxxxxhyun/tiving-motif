package com.converter.controller;

import com.converter.service.ConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/convert")
@RequiredArgsConstructor
public class ConverterController {
    private final ConverterService converterService;

    @GetMapping("/files/{filename}")
    public ResponseEntity<String> load(@PathVariable("filename") String filename) {
        converterService.convertToHls(filename);
        return ResponseEntity.ok().body("convert success");
    }
}
