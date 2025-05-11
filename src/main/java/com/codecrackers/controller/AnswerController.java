package com.codecrackers.controller;

import com.codecrackers.dto.AnswerResponseDTO;
import com.codecrackers.model.Answer;
import com.codecrackers.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin(origins = "*")
public class AnswerController {

    @Autowired
    private AnswerService answerService;
    
    /**
     * Get answers for a specific doubt/query
     * @param doubtId The ID of the doubt/query
     * @return ResponseEntity containing an AnswerResponseDTO
     */
    @GetMapping("/query/{doubtId}")
    public ResponseEntity<AnswerResponseDTO> getAnswersForDoubt(@PathVariable Long doubtId) {
        AnswerResponseDTO response = answerService.getAnswersForDoubt(doubtId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Create a new answer for a doubt
     * @param answer The answer to create
     * @param doubtId The ID of the doubt
     * @param studentId The ID of the student creating the answer
     * @return ResponseEntity containing the created answer
     */
    @PostMapping("/doubt/{doubtId}/student/{studentId}")
    public ResponseEntity<Answer> createAnswer(
            @RequestBody Answer answer,
            @PathVariable Long doubtId,
            @PathVariable Long studentId) {
        Answer createdAnswer = answerService.createAnswer(answer, doubtId, studentId);
        return new ResponseEntity<>(createdAnswer, HttpStatus.CREATED);
    }
    
    /**
     * Upvote an answer
     * @param answerId The ID of the answer to upvote
     * @return ResponseEntity containing the updated answer
     */
    @PostMapping("/{answerId}/upvote")
    public ResponseEntity<Answer> upvoteAnswer(@PathVariable Long answerId) {
        Answer updatedAnswer = answerService.upvoteAnswer(answerId);
        return ResponseEntity.ok(updatedAnswer);
    }
    
    /**
     * Downvote an answer
     * @param answerId The ID of the answer to downvote
     * @return ResponseEntity containing the updated answer
     */
    @PostMapping("/{answerId}/downvote")
    public ResponseEntity<Answer> downvoteAnswer(@PathVariable Long answerId) {
        Answer updatedAnswer = answerService.downvoteAnswer(answerId);
        return ResponseEntity.ok(updatedAnswer);
    }
    
    /**
     * Mark an answer as verified
     * @param answerId The ID of the answer to verify
     * @param isVerified Whether the answer is verified
     * @return ResponseEntity containing the updated answer
     */
    @PostMapping("/{answerId}/verify")
    public ResponseEntity<Answer> verifyAnswer(
            @PathVariable Long answerId,
            @RequestParam(value = "verified", defaultValue = "true") boolean isVerified) {
        Answer updatedAnswer = answerService.verifyAnswer(answerId, isVerified);
        return ResponseEntity.ok(updatedAnswer);
    }
} 