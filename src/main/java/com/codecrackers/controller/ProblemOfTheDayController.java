package com.codecrackers.controller;

import com.codecrackers.dto.MonthlyProblemStatusDTO;
import com.codecrackers.dto.ProblemOfTheDayRequestDTO;
import com.codecrackers.model.ProblemOfTheDay;
import com.codecrackers.service.ProblemOfTheDayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/problem-of-the-day")
public class ProblemOfTheDayController {

    @Autowired
    private ProblemOfTheDayService service;
    
    @PostMapping
    public ResponseEntity<ProblemOfTheDay> createProblemOfTheDay(
            @Valid @RequestBody ProblemOfTheDayRequestDTO requestDTO) {
        ProblemOfTheDay createdProblem = service.createProblemOfTheDay(requestDTO);
        return new ResponseEntity<>(createdProblem, HttpStatus.CREATED);
    }
    
    @GetMapping("/{date}")
    public ResponseEntity<ProblemOfTheDay> getProblemOfTheDay(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getProblemOfTheDay(date));
    }
    
    @GetMapping("/status/{email}/{date}")
    public ResponseEntity<Boolean> isProblemSolved(
            @PathVariable String email,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.isProblemSolved(email, date));
    }
    
    @PostMapping("/mark-solved/{email}/{date}")
    public ResponseEntity<Boolean> markProblemAsSolved(
            @PathVariable String email,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.markProblemAsSolved(email, date));
    }
    
    @GetMapping("/monthly-status/{email}/{year}/{month}")
    public ResponseEntity<MonthlyProblemStatusDTO> getMonthlyProblemStatus(
            @PathVariable String email,
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(service.getMonthlyProblemStatus(email, year, month));
    }
}