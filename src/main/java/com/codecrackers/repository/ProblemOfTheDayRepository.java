package com.codecrackers.repository;

import com.codecrackers.model.ProblemOfTheDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ProblemOfTheDayRepository extends JpaRepository<ProblemOfTheDay, Long> {
    Optional<ProblemOfTheDay> findByDate(LocalDate date);
} 