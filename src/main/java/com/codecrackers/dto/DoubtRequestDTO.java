package com.codecrackers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubtRequestDTO {
    private String title;
    private String category;
    private String description;
    private String code;
    private List<String> tags = new ArrayList<>();
} 