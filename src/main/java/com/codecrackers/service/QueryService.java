package com.codecrackers.service;

import com.codecrackers.dto.QueryResponseDTO;
import com.codecrackers.dto.SubmitQueryRequestDTO;
import com.codecrackers.model.Doubt;

public interface QueryService {
    
    /**
     * Get a paginated list of queries
     * @param page The page number (starting from 1)
     * @param limit The maximum number of queries per page
     * @return QueryResponseDTO containing the list of queries
     */
    QueryResponseDTO getQueries(int page, int limit);
    
    /**
     * Get a paginated list of queries by category
     * @param category The category to filter by
     * @param page The page number (starting from 1)
     * @param limit The maximum number of queries per page
     * @return QueryResponseDTO containing the list of queries
     */
    QueryResponseDTO getQueriesByCategory(String category, int page, int limit);
    
    /**
     * Submit a new query
     * @param requestDTO The query submission request
     * @param studentId The ID of the student submitting the query
     * @return The created doubt/query
     */
    Doubt submitQuery(SubmitQueryRequestDTO requestDTO, Long studentId);
} 