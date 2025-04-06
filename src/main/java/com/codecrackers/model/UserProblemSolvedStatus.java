package com.codecrackers.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_problem_solved_status",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}))
public class UserProblemSolvedStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student user;
    
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
    
    public Student getUser() {
        return user;
    }
    
    public void setUser(Student user) {
        this.user = user;
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