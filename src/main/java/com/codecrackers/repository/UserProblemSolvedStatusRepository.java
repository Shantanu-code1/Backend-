package com.codecrackers.repository;

import com.codecrackers.model.UserProblemSolvedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProblemSolvedStatusRepository extends JpaRepository<UserProblemSolvedStatus, Long> {
    Optional<UserProblemSolvedStatus> findByEmailAndDate(String email, LocalDate date);
    
    @Query("SELECT u FROM UserProblemSolvedStatus u WHERE u.email = :email AND u.date BETWEEN :startDate AND :endDate")
    List<UserProblemSolvedStatus> findByEmailAndDateBetween(
            @Param("email") String email, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
} 