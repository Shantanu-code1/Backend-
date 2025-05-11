package com.codecrackers.service;

import com.codecrackers.dto.RecentDoubtDTO;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.IsSolvedDoubt;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoubtServiceImpl implements DoubtService {
    
    @Autowired
    private DoubtRepository doubtRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final int HOURS_THRESHOLD_FOR_NEW = 24; // Doubts less than 24 hours old are considered new
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubts(int limit) {
        List<Doubt> recentDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
        return convertToRecentDoubtDTOs(recentDoubts);
    }
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubtsByCategory(String category, int limit) {
        List<Doubt> categoryDoubts = doubtRepository.findRecentDoubtsByCategory(category);
        
        // Apply limit manually if not using a native query with LIMIT
        if (categoryDoubts.size() > limit) {
            categoryDoubts = categoryDoubts.subList(0, limit);
        }
        
        return convertToRecentDoubtDTOs(categoryDoubts);
    }
    
    @Override
    public List<RecentDoubtDTO> getRecentDoubtsByFilter(String filter, int limit) {
        List<Doubt> filteredDoubts;
        
        switch (filter.toLowerCase()) {
            case "doubts":
                filteredDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
                break;
            case "queries":
                // You can implement different filters based on your needs
                filteredDoubts = doubtRepository.findByIsSolved(IsSolvedDoubt.PENDING);
                break;
            case "ai":
                // For AI-related doubts or other custom filters
                filteredDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
                // You might want to add additional filtering logic here
                break;
            default:
                filteredDoubts = doubtRepository.findRecentDoubtsWithLimit(limit);
        }
        
        // Apply limit manually if needed
        if (filteredDoubts.size() > limit) {
            filteredDoubts = filteredDoubts.subList(0, limit);
        }
        
        return convertToRecentDoubtDTOs(filteredDoubts);
    }
    
    @Override
    public List<Doubt> getAllDoubts() {
        return doubtRepository.findAll();
    }
    
    private List<RecentDoubtDTO> convertToRecentDoubtDTOs(List<Doubt> doubts) {
        List<RecentDoubtDTO> dtoList = new ArrayList<>();
        
        for (Doubt doubt : doubts) {
            RecentDoubtDTO dto = new RecentDoubtDTO();
            dto.setId(doubt.getId());
            
            // Set the title (using doubt or topic field)
            dto.setTitle(doubt.getDoubt() != null ? doubt.getDoubt() : "Untitled Doubt");
            
            // Set category from topic field
            dto.setCategory(doubt.getTopic() != null ? doubt.getTopic() : "General");
            
            // Format the time as a human-readable relative time
            dto.setTime(TimeUtil.getRelativeTimeFromNow(doubt.getTimeSubmitted()));
            
            // Set status based on IsSolvedDoubt enum
            dto.setStatus(mapSolvedStatus(doubt.getIsSolved()));
            
            // Use a random placeholder value for replies until the doubt_replies table is created
            // Later you can replace this with the actual implementation
            // dto.setReplies(doubtRepository.countRepliesForDoubt(doubt.getId()));
            dto.setReplies((int) (Math.random() * 5)); // Random number of replies between 0-4
            
            // Check if doubt is new (less than 24 hours old)
            dto.setNew(isRecentlyCreated(doubt.getTimeSubmitted()));
            
            dtoList.add(dto);
        }
        
        return dtoList;
    }
    
    private String mapSolvedStatus(IsSolvedDoubt status) {
        if (status == null) {
            return "Pending";
        }
        
        switch (status) {
            case DONE:
                return "Answered";
            case IN_PROGRESS:
                return "In Progress";
            case PENDING:
            default:
                return "Pending";
        }
    }
    
    private boolean isRecentlyCreated(String timestamp) {
        try {
            LocalDateTime doubtTime = LocalDateTime.parse(timestamp, DATE_FORMATTER);
            LocalDateTime now = LocalDateTime.now();
            
            // Calculate hours difference
            long hoursDiff = java.time.Duration.between(doubtTime, now).toHours();
            
            return hoursDiff < HOURS_THRESHOLD_FOR_NEW;
        } catch (Exception e) {
            // In case of parsing error, default to not new
            return false;
        }
    }
} 