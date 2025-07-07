package com.major.domain.dto;

public class Upload {
    public record Response(
            String originalFileName,
            String fileExtension,
            String uniqueFileName
    ){

    }
}
