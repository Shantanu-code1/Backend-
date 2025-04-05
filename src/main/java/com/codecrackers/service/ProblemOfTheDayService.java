package com.codecrackers.service;

import com.codecrackers.dto.MonthlyProblemStatusDTO;
import com.codecrackers.dto.ProblemOfTheDayRequestDTO;
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
    
    public boolean markProblemAsSolved(String email, LocalDate date) {
        problemRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("No problem found for date: " + date));
        
        Optional<UserProblemSolvedStatus> existingStatus = statusRepository.findByEmailAndDate(email, date);
        
        UserProblemSolvedStatus status;
        if (existingStatus.isPresent()) {
            status = existingStatus.get();
            status.setSolved(true);
        } else {
            status = new UserProblemSolvedStatus();
            status.setEmail(email);
            status.setDate(date);
            status.setSolved(true);
        }
        
        statusRepository.save(status);
        return true;
    }
    
    public boolean isProblemSolved(String email, LocalDate date) {
        return statusRepository.findByEmailAndDate(email, date)
                .map(UserProblemSolvedStatus::isSolved)
                .orElse(false);
    }
    
    public MonthlyProblemStatusDTO getMonthlyProblemStatus(String email, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<UserProblemSolvedStatus> monthlyStatuses = 
                statusRepository.findByEmailAndDateBetween(email, startDate, endDate);
        
        Map<Integer, Boolean> dailyStatus = new HashMap<>();
        
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            dailyStatus.put(day, false);
        }
        
        for (UserProblemSolvedStatus status : monthlyStatuses) {
            if (status.isSolved()) {
                dailyStatus.put(status.getDate().getDayOfMonth(), true);
            }
        }
        
        return new MonthlyProblemStatusDTO(year, month, dailyStatus);
    }

    public ProblemOfTheDay createProblemOfTheDay(ProblemOfTheDayRequestDTO requestDTO) {
        // Check if a problem already exists for this date
        Optional<ProblemOfTheDay> existingProblem = problemRepository.findByDate(requestDTO.getDate());
        
        if (existingProblem.isPresent()) {
            throw new RuntimeException("A problem already exists for date: " + requestDTO.getDate());
        }
        
        // Create new problem
        ProblemOfTheDay newProblem = new ProblemOfTheDay();
        newProblem.setDate(requestDTO.getDate());
        newProblem.setTitle(requestDTO.getTitle());
        newProblem.setDescription(requestDTO.getDescription());
        
        return problemRepository.save(newProblem);
    }
} 