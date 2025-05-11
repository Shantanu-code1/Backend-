package com.codecrackers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDTO {
    private boolean success = true;
    private AnswerDataDTO data;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDataDTO {
        private List<AnswerDTO> answers;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        private Long id;
        private Long queryId;
        private AuthorDTO author;
        private String content;
        private String codeSnippet;
        private String date;
        private VotesDTO votes;
        private boolean isVerified;
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
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VotesDTO {
        private int up;
        private int down;
    }
} 