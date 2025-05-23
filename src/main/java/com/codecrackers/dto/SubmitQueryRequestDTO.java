package com.codecrackers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitQueryRequestDTO {
    private String title;
    private String body;
    private String codeSnippet;
    private String category;
    private List<String> tags = new ArrayList<>();
} 