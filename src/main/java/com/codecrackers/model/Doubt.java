package com.codecrackers.model;

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
@Table(name = "DOUBTS")
public class Doubt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    private String description;
    private String doubt;
    private IsSolvedDoubt isSolved = IsSolvedDoubt.PENDING;
    private String doubtImage;

    @ManyToOne
    private Student student;

    private String timeSubmitted;
}
