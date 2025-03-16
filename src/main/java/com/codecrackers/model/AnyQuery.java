package com.codecrackers.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ANY_QUERY")
public class AnyQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String query;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
