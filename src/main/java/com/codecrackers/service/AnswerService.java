package com.codecrackers.service;

import com.codecrackers.dto.AnswerResponseDTO;
import com.codecrackers.model.Answer;

public interface AnswerService {
    
    /**
     * Get answers for a specific doubt/query
     * @param doubtId The ID of the doubt/query
     * @return AnswerResponseDTO containing the list of answers
     */
    AnswerResponseDTO getAnswersForDoubt(Long doubtId);
    
    /**
     * Create a new answer for a doubt
     * @param answer The answer to create
     * @param doubtId The ID of the doubt
     * @param studentId The ID of the student creating the answer
     * @return The created answer
     */
    Answer createAnswer(Answer answer, Long doubtId, Long studentId);
    
    /**
     * Upvote an answer
     * @param answerId The ID of the answer to upvote
     * @return The updated answer
     */
    Answer upvoteAnswer(Long answerId);
    
    /**
     * Downvote an answer
     * @param answerId The ID of the answer to downvote
     * @return The updated answer
     */
    Answer downvoteAnswer(Long answerId);
    
    /**
     * Mark an answer as verified
     * @param answerId The ID of the answer to verify
     * @param isVerified Whether the answer is verified
     * @return The updated answer
     */
    Answer verifyAnswer(Long answerId, boolean isVerified);
} 