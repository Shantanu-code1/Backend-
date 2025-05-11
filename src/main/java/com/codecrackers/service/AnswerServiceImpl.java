package com.codecrackers.service;

import com.codecrackers.dto.AnswerResponseDTO;
import com.codecrackers.model.Answer;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.Student;
import com.codecrackers.model.USER_ROLE;
import com.codecrackers.repository.AnswerRepository;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private DoubtRepository doubtRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public AnswerResponseDTO getAnswersForDoubt(Long doubtId) {
        List<Answer> answers = answerRepository.findByDoubtIdOrderByCreatedAtDesc(doubtId);
        
        // Convert to DTO
        List<AnswerResponseDTO.AnswerDTO> answerDTOs = new ArrayList<>();
        
        for (Answer answer : answers) {
            AnswerResponseDTO.AnswerDTO dto = new AnswerResponseDTO.AnswerDTO();
            dto.setId(answer.getId());
            dto.setQueryId(answer.getDoubt().getId());
            
            // Set author information
            Student student = answer.getStudent();
            AnswerResponseDTO.AuthorDTO authorDTO = new AnswerResponseDTO.AuthorDTO();
            authorDTO.setId(student.getId());
            authorDTO.setName(student.getName());
            authorDTO.setAvatar(student.getProfileImage() != null ? student.getProfileImage() : "/placeholder.svg");
            
            // Set role
            String role = student.getRole() == USER_ROLE.ROLE_TEACHER ? "Teacher" : "Student";
            authorDTO.setRole(role);
            
            dto.setAuthor(authorDTO);
            
            // Set content and code snippet
            dto.setContent(answer.getContent());
            dto.setCodeSnippet(answer.getCodeSnippet());
            
            // Format date
            dto.setDate(answer.getCreatedAt().format(DATE_FORMATTER));
            
            // Set votes
            AnswerResponseDTO.VotesDTO votesDTO = new AnswerResponseDTO.VotesDTO();
            votesDTO.setUp(answer.getUpVotes());
            votesDTO.setDown(answer.getDownVotes());
            dto.setVotes(votesDTO);
            
            // Set verified status
            dto.setVerified(answer.isVerified());
            
            answerDTOs.add(dto);
        }
        
        // Create response DTO
        AnswerResponseDTO responseDTO = new AnswerResponseDTO();
        AnswerResponseDTO.AnswerDataDTO dataDTO = new AnswerResponseDTO.AnswerDataDTO();
        dataDTO.setAnswers(answerDTOs);
        responseDTO.setData(dataDTO);
        
        return responseDTO;
    }
    
    @Override
    public Answer createAnswer(Answer answer, Long doubtId, Long studentId) {
        Optional<Doubt> doubtOpt = doubtRepository.findById(doubtId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (doubtOpt.isPresent() && studentOpt.isPresent()) {
            answer.setDoubt(doubtOpt.get());
            answer.setStudent(studentOpt.get());
            return answerRepository.save(answer);
        }
        
        return null;
    }
    
    @Override
    public Answer upvoteAnswer(Long answerId) {
        Optional<Answer> answerOpt = answerRepository.findById(answerId);
        
        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();
            answer.setUpVotes(answer.getUpVotes() + 1);
            return answerRepository.save(answer);
        }
        
        return null;
    }
    
    @Override
    public Answer downvoteAnswer(Long answerId) {
        Optional<Answer> answerOpt = answerRepository.findById(answerId);
        
        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();
            answer.setDownVotes(answer.getDownVotes() + 1);
            return answerRepository.save(answer);
        }
        
        return null;
    }
    
    @Override
    public Answer verifyAnswer(Long answerId, boolean isVerified) {
        Optional<Answer> answerOpt = answerRepository.findById(answerId);
        
        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();
            answer.setVerified(isVerified);
            return answerRepository.save(answer);
        }
        
        return null;
    }
} 