package com.Optimart.dto.FirebaseCloud;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Note {
    private String context;
    private String subject;
    private String content;
//    private Map<String, String> data;
}
