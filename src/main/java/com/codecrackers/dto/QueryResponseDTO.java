package com.codecrackers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponseDTO {
    private boolean success = true;
    private QueryDataDTO data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDataDTO {
        private List<QueryDTO> queries;
        private int total;
        private int page;
        private int limit;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryDTO {
        private Long id;
        private String title;
        private String body;
        private String codeSnippet;
        private AuthorDTO author;
        private String category;
        private List<String> tags;
        private String date;
        private int views;
        private int answers;
        private int votes;
        private String status;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDTO {
        private Long id;
        private String name;
        private String avatar;
        private String role;
    }
} 