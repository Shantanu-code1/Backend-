package com.codecrackers.dto;

import java.util.Map;

public class MonthlyProblemStatusDTO {
    private int year;
    private int month;
    private Map<Integer, Boolean> dailyStatus; // day of month -> solved status
    
    public MonthlyProblemStatusDTO() {
    }
    
    public MonthlyProblemStatusDTO(int year, int month, Map<Integer, Boolean> dailyStatus) {
        this.year = year;
        this.month = month;
        this.dailyStatus = dailyStatus;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public Map<Integer, Boolean> getDailyStatus() {
        return dailyStatus;
    }
    
    public void setDailyStatus(Map<Integer, Boolean> dailyStatus) {
        this.dailyStatus = dailyStatus;
    }
} 