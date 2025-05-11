package com.codecrackers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "ANSWERS")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "doubt_id")
    @JsonBackReference
    private Doubt doubt;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonBackReference(value = "student-answer")
    private Student student;
    
    @Column(length = 5000)
    private String content;
    
    @Column(length = 5000)
    private String codeSnippet;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private int upVotes = 0;
    private int downVotes = 0;
    
    private boolean isVerified = false;
} 