package com.codecrackers.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_problem_solved_status",
       uniqueConstraints = @UniqueConstraint(columnNames = {"email", "date"}))
public class UserProblemSolvedStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private boolean solved;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public boolean isSolved() {
        return solved;
    }
    
    public void setSolved(boolean solved) {
        this.solved = solved;
    }
} 