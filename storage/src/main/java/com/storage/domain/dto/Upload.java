package com.storage.domain.dto;

public class Upload {
    public record Response(
            String originalFileName,
            String fileExtension,
            String uniqueFileName
    ){
        public static Response of(
                String originalFileName,
                String fileExtension,
                String uniqueFileName
        ){
            return new Response(
                    originalFileName,
                    fileExtension,
                    uniqueFileName
            );
        }
    }
}
