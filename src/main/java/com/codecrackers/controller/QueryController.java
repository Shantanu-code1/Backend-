package com.codecrackers.controller;

import com.codecrackers.dto.QueryResponseDTO;
import com.codecrackers.dto.SubmitQueryRequestDTO;
import com.codecrackers.model.Doubt;
import com.codecrackers.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "*")
public class QueryController {

    @Autowired
    private QueryService queryService;
    
    private static final int DEFAULT_LIMIT = 10;
    private static final int DEFAULT_PAGE = 1;
    
    /**
     * Get a paginated list of queries
     * @param page The page number (starting from 1)
     * @param limit The maximum number of queries per page
     * @return ResponseEntity containing a QueryResponseDTO
     */
    @GetMapping
    public ResponseEntity<QueryResponseDTO> getQueries(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        QueryResponseDTO response = queryService.getQueries(page, limit);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get a paginated list of queries by category
     * @param category The category to filter by
     * @param page The page number (starting from 1)
     * @param limit The maximum number of queries per page
     * @return ResponseEntity containing a QueryResponseDTO
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<QueryResponseDTO> getQueriesByCategory(
            @PathVariable String category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        QueryResponseDTO response = queryService.getQueriesByCategory(category, page, limit);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Submit a new query
     * @param requestDTO The query submission request
     * @param studentId The ID of the student submitting the query
     * @return ResponseEntity containing the created doubt/query
     */
    @PostMapping("/student/{studentId}")
    public ResponseEntity<Doubt> submitQuery(
            @RequestBody SubmitQueryRequestDTO requestDTO,
            @PathVariable Long studentId) {
        Doubt createdDoubt = queryService.submitQuery(requestDTO, studentId);
        
        if (createdDoubt != null) {
            return new ResponseEntity<>(createdDoubt, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
} 