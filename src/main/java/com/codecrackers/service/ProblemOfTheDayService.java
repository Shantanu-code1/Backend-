package com.codecrackers.service;

import com.codecrackers.dto.MonthlyProblemStatusDTO;
import com.codecrackers.model.ProblemOfTheDay;
import com.codecrackers.model.UserProblemSolvedStatus;
import com.codecrackers.repository.ProblemOfTheDayRepository;
import com.codecrackers.repository.UserProblemSolvedStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProblemOfTheDayService {

    @Autowired
    private ProblemOfTheDayRepository problemRepository;
    
    @Autowired
    private UserProblemSolvedStatusRepository statusRepository;
    
    public ProblemOfTheDay getProblemOfTheDay(LocalDate date) {
        return problemRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("No problem found for date: " + date));
    }
    
    public boolean markProblemAsSolved(Long userId, LocalDate date) {
        // Check if problem exists for the given date
        problemRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("No problem found for date: " + date));
        
        // Find or create status record
        Optional<UserProblemSolvedStatus> existingStatus = statusRepository.findByUserIdAndDate(userId, date);
        
        UserProblemSolvedStatus status;
        if (existingStatus.isPresent()) {
            status = existingStatus.get();
            status.setSolved(true);
        } else {
            status = new UserProblemSolvedStatus();
            status.setUserId(userId);
            status.setDate(date);
            status.setSolved(true);
        }
        
        statusRepository.save(status);
        return true;
    }
    
    public boolean isProblemSolved(Long userId, LocalDate date) {
        return statusRepository.findByUserIdAndDate(userId, date)
                .map(UserProblemSolvedStatus::isSolved)
                .orElse(false);
    }
    
    public MonthlyProblemStatusDTO getMonthlyProblemStatus(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        // Get all user's solved status for the month
        List<UserProblemSolvedStatus> monthlyStatuses = 
                statusRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
        
        // Map day of month to solved status
        Map<Integer, Boolean> dailyStatus = new HashMap<>();
        
        // Initialize all days as false (unsolved)
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            dailyStatus.put(day, false);
        }
        
        // Update with actual solved statuses
        for (UserProblemSolvedStatus status : monthlyStatuses) {
            if (status.isSolved()) {
                dailyStatus.put(status.getDate().getDayOfMonth(), true);
            }
        }
        
        return new MonthlyProblemStatusDTO(year, month, dailyStatus);
    }
} 