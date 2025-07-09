package com.major.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyMessage {
    private String filename;

    public static MyMessage of(String filename){
        return new MyMessage(filename);
    }
}
