package com.codecrackers.controller;

import com.codecrackers.dto.RecentDoubtDTO;
import com.codecrackers.service.DoubtService;
import com.codecrackers.model.Doubt;
import com.codecrackers.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/doubts")
@CrossOrigin(origins = "*")
public class DoubtController {

    @Autowired
    private DoubtService doubtService;
    
    @Autowired
    private StudentService studentService;
    
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
    
    /**
     * Get a list of doubts for a specific user by ID
     * @param userId The ID of the user
     * @return ResponseEntity containing a list of Doubt objects
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Doubt>> getDoubtsByUserId(@PathVariable Long userId) {
        List<Doubt> userDoubts = doubtService.getDoubtsByUserId(userId);
        return ResponseEntity.ok(userDoubts);
    }
    
    /**
     * Get a list of doubts for a specific user by email
     * @param email The email of the user
     * @return ResponseEntity containing a list of Doubt objects
     */
    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<Doubt>> getDoubtsByUserEmail(@PathVariable String email) {
        List<Doubt> userDoubts = doubtService.getDoubtsByUserEmail(email);
        return ResponseEntity.ok(userDoubts);
    }

    /**
     * Initiates an answer session for a doubt and notifies the student.
     * @param doubtId The ID of the doubt.
     * @param jwt The JWT of the user initiating the session (teacher).
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/{doubtId}/initiate-answer-session")
    public ResponseEntity<String> initiateAnswerSession(
            @PathVariable Long doubtId,
            @RequestHeader("Authorization") String jwt) {
        try {
            // We need to remove "Bearer " prefix from JWT
            String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
            doubtService.initiateAnswerSession(doubtId, token);
            return ResponseEntity.ok("Notification email sent to the student.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to initiate answer session: " + e.getMessage());
        }
    }
} 