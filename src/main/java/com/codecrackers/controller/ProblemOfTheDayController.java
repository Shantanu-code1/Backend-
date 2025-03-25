package com.codecrackers.controller;

import com.codecrackers.dto.MonthlyProblemStatusDTO;
import com.codecrackers.model.ProblemOfTheDay;
import com.codecrackers.service.ProblemOfTheDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/problem-of-the-day")
public class ProblemOfTheDayController {

    @Autowired
    private ProblemOfTheDayService service;
    
    @GetMapping("/{date}")
    public ResponseEntity<ProblemOfTheDay> getProblemOfTheDay(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getProblemOfTheDay(date));
    }
    
    @GetMapping("/status/{userId}/{date}")
    public ResponseEntity<Boolean> isProblemSolved(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.isProblemSolved(userId, date));
    }
    
    @PostMapping("/mark-solved/{userId}/{date}")
    public ResponseEntity<Boolean> markProblemAsSolved(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.markProblemAsSolved(userId, date));
    }
    
    @GetMapping("/monthly-status/{userId}/{year}/{month}")
    public ResponseEntity<MonthlyProblemStatusDTO> getMonthlyProblemStatus(
            @PathVariable Long userId,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(service.getMonthlyProblemStatus(userId, year, month));
    }
} 