package com.codecrackers.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
    
    /**
     * Converts a timestamp to a human-readable relative time (e.g., "2 hours ago")
     * 
     * @param timestamp The timestamp to format (could be in various formats)
     * @return A human-readable relative time string
     */
    public static String getRelativeTimeFromNow(String timestamp) {
        try {
            // First try parsing as a LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            return getRelativeTimeFromNow(dateTime);
        } catch (Exception e) {
            // If unable to parse, return the original timestamp
            return timestamp;
        }
    }
    
    /**
     * Converts a LocalDateTime to a human-readable relative time
     * 
     * @param dateTime The LocalDateTime to format
     * @return A human-readable relative time string
     */
    public static String getRelativeTimeFromNow(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (minutes < 1) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes != 1 ? "s" : "") + " ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours != 1 ? "s" : "") + " ago";
        } else if (days < 30) {
            return days + " day" + (days != 1 ? "s" : "") + " ago";
        } else {
            long months = ChronoUnit.MONTHS.between(dateTime.toLocalDate(), now.toLocalDate());
            if (months < 12) {
                return months + " month" + (months != 1 ? "s" : "") + " ago";
            } else {
                long years = ChronoUnit.YEARS.between(dateTime.toLocalDate(), now.toLocalDate());
                return years + " year" + (years != 1 ? "s" : "") + " ago";
            }
        }
    }
} 