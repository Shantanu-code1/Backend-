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
}
