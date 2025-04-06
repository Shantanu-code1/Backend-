package com.codecrackers.controller;

import com.codecrackers.dto.RecentDoubtDTO;
import com.codecrackers.service.DoubtService;
import com.codecrackers.model.Doubt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doubts")
@CrossOrigin(origins = "*")
public class DoubtController {

    @Autowired
    private DoubtService doubtService;
    
    private static final int DEFAULT_LIMIT = 10;

    /**
     * Get a list of recent doubts
     * @return ResponseEntity containing a list of RecentDoubtDTO objects
     */
    @GetMapping("/recent")
    public ResponseEntity<List<RecentDoubtDTO>> getRecentDoubts(
            @RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<RecentDoubtDTO> recentDoubts = doubtService.getRecentDoubts(limit);
        return ResponseEntity.ok(recentDoubts);
    }
    
    /**
     * Get a list of recent doubts filtered by category
     * @param category The category to filter by
     * @return ResponseEntity containing a list of RecentDoubtDTO objects
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<RecentDoubtDTO>> getDoubtsByCategory(
            @PathVariable String category,
            @RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<RecentDoubtDTO> categoryDoubts = doubtService.getRecentDoubtsByCategory(category, limit);
        return ResponseEntity.ok(categoryDoubts);
    }
    
    /**
     * Get a list of recent doubts filtered by type (doubts, queries, ai)
     * @param filter The filter to apply
     * @return ResponseEntity containing a list of RecentDoubtDTO objects
     */
    @GetMapping("/filter/{filter}")
    public ResponseEntity<List<RecentDoubtDTO>> getDoubtsByFilter(
            @PathVariable String filter,
            @RequestParam(value = "limit", defaultValue = "3") int limit) {
        List<RecentDoubtDTO> filteredDoubts = doubtService.getRecentDoubtsByFilter(filter, limit);
        return ResponseEntity.ok(filteredDoubts);
    }
    
    /**
     * Get a list of all doubts
     * @return ResponseEntity containing a list of Doubt objects
     */
    @GetMapping
    public ResponseEntity<List<Doubt>> getAllDoubts() {
        List<Doubt> allDoubts = doubtService.getAllDoubts();
        return ResponseEntity.ok(allDoubts);
    }
} 