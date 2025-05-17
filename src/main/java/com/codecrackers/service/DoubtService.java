package com.codecrackers.service;

import com.codecrackers.dto.RecentDoubtDTO;
import com.codecrackers.model.Doubt;

import java.util.List;

public interface DoubtService {
    /**
     * Get a list of recent doubts with a limit
     * @param limit The maximum number of doubts to return
     * @return List of RecentDoubtDTO objects
     */
    List<RecentDoubtDTO> getRecentDoubts(int limit);
    
    /**
     * Get a list of recent doubts by category with a limit
     * @param category The category to filter by
     * @param limit The maximum number of doubts to return
     * @return List of RecentDoubtDTO objects
     */
    List<RecentDoubtDTO> getRecentDoubtsByCategory(String category, int limit);
    
    /**
     * Get a list of recent doubts by filter (doubts, queries, ai) with a limit
     * @param filter The filter to apply (doubts, queries, ai)
     * @param limit The maximum number of doubts to return
     * @return List of RecentDoubtDTO objects
     */
    List<RecentDoubtDTO> getRecentDoubtsByFilter(String filter, int limit);

    /**
     * Get a list of all doubts
     * @return List of Doubt objects
     */
    List<Doubt> getAllDoubts();
    
    /**
     * Get a list of doubts for a specific user
     * @param userId The ID of the user
     * @return List of Doubt objects
     */
    List<Doubt> getDoubtsByUserId(Long userId);
    
    /**
     * Get a list of doubts for a specific user by email
     * @param email The email of the user
     * @return List of Doubt objects
     */
    List<Doubt> getDoubtsByUserEmail(String email);

    /**
     * Initiates an answer session for a doubt and notifies the student via email.
     * @param doubtId The ID of the doubt.
     * @param initiatorJwtToken The JWT of the user initiating the session (e.g., teacher).
     * @throws Exception If any error occurs during the process.
     */
    void initiateAnswerSession(Long doubtId, String initiatorJwtToken) throws Exception;
}
