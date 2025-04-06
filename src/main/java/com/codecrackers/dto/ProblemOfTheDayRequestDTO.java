package com.codecrackers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ProblemOfTheDayRequestDTO {
    
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private String difficulty;
    
    private String category;
    
    private String exampleInput;
    
    private String exampleOutput;
    
    private String timeComplexity;
    
    private String spaceComplexity;
    
    private Integer likes;
    
    private Integer dislikes;
    
    private Integer submissions;
    
    private Double acceptanceRate;
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getExampleInput() {
        return exampleInput;
    }
    
    public void setExampleInput(String exampleInput) {
        this.exampleInput = exampleInput;
    }
    
    public String getExampleOutput() {
        return exampleOutput;
    }
    
    public void setExampleOutput(String exampleOutput) {
        this.exampleOutput = exampleOutput;
    }
    
    public String getTimeComplexity() {
        return timeComplexity;
    }
    
    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }
    
    public String getSpaceComplexity() {
        return spaceComplexity;
    }
    
    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }
    
    public Integer getLikes() {
        return likes;
    }
    
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    
    public Integer getDislikes() {
        return dislikes;
    }
    
    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }
    
    public Integer getSubmissions() {
        return submissions;
    }
    
    public void setSubmissions(Integer submissions) {
        this.submissions = submissions;
    }
    
    public Double getAcceptanceRate() {
        return acceptanceRate;
    }
    
    public void setAcceptanceRate(Double acceptanceRate) {
        this.acceptanceRate = acceptanceRate;
    }
} 