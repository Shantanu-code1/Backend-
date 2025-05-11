package com.codecrackers.repository;

import com.codecrackers.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    // Find answers by doubt ID
    List<Answer> findByDoubtIdOrderByCreatedAtDesc(Long doubtId);
    
    // Find answers by student ID
    List<Answer> findByStudentId(Long studentId);
    
    // Count answers for a doubt
    @Query("SELECT COUNT(a) FROM Answer a WHERE a.doubt.id = :doubtId")
    int countAnswersForDoubt(@Param("doubtId") Long doubtId);
} 