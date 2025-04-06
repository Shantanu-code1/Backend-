package com.codecrackers.repository;

import com.codecrackers.model.Student;
import com.codecrackers.model.UserProblemSolvedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProblemSolvedStatusRepository extends JpaRepository<UserProblemSolvedStatus, Long> {
    Optional<UserProblemSolvedStatus> findByUserAndDate(Student user, LocalDate date);
    
    List<UserProblemSolvedStatus> findByUserAndDateBetween(Student user, LocalDate startDate, LocalDate endDate);
} 