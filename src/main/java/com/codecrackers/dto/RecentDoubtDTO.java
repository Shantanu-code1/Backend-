package com.codecrackers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentDoubtDTO {
    private Long id;
    private String title;
    private String category;
    private String time;
    private String status;
    private int replies;
    private boolean isNew;
} 