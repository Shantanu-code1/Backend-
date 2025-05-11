package com.codecrackers.service;

import com.codecrackers.dto.QueryResponseDTO;
import com.codecrackers.dto.SubmitQueryRequestDTO;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.DoubtType;
import com.codecrackers.model.IsSolvedDoubt;
import com.codecrackers.model.Student;
import com.codecrackers.model.USER_ROLE;
import com.codecrackers.repository.AnswerRepository;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private DoubtRepository doubtRepository;
    
    @Autowired
    private AnswerRepository answerRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Random random = new Random();
    
    @Override
    public QueryResponseDTO getQueries(int page, int limit) {
        // Adjust page for Spring's 0-based indexing
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "timeSubmitted"));
        Page<Doubt> doubtsPage = doubtRepository.findByType(DoubtType.QUERY, pageable);
        
        return createQueryResponseDTO(doubtsPage, page, limit);
    }
    
    @Override
    public QueryResponseDTO getQueriesByCategory(String category, int page, int limit) {
        // Get all queries by category
        List<Doubt> categoryDoubts = doubtRepository.findRecentDoubtsByCategoryAndType(category, DoubtType.QUERY);
        
        // Simulate pagination
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, categoryDoubts.size());
        
        List<Doubt> paginatedDoubts = start < categoryDoubts.size() ? 
                                      categoryDoubts.subList(start, end) : 
                                      new ArrayList<>();
        
        // Create the response DTO
        QueryResponseDTO responseDTO = new QueryResponseDTO();
        QueryResponseDTO.QueryDataDTO dataDTO = new QueryResponseDTO.QueryDataDTO();
        
        List<QueryResponseDTO.QueryDTO> queryDTOs = convertDoubtsToQueryDTOs(paginatedDoubts);
        
        dataDTO.setQueries(queryDTOs);
        dataDTO.setTotal(categoryDoubts.size());
        dataDTO.setPage(page);
        dataDTO.setLimit(limit);
        
        responseDTO.setData(dataDTO);
        
        return responseDTO;
    }
    
    @Override
    public Doubt submitQuery(SubmitQueryRequestDTO requestDTO, Long studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        
        if (studentOpt.isPresent()) {
            Doubt doubt = new Doubt();
            
            // Set basic fields
            doubt.setTitle(requestDTO.getTitle());
            doubt.setDescription(requestDTO.getBody());
            doubt.setCodeSnippet(requestDTO.getCodeSnippet());
            doubt.setTopic(requestDTO.getCategory());
            
            // Set tags
            doubt.setTagsFromList(requestDTO.getTags());
            
            // Set type as QUERY
            doubt.setType(DoubtType.QUERY);
            
            // Set defaults
            doubt.setIsSolved(IsSolvedDoubt.PENDING);
            doubt.setStudent(studentOpt.get());
            
            // Set current timestamp
            LocalDateTime now = LocalDateTime.now();
            doubt.setTimeSubmitted(now.format(DateTimeFormatter.ISO_DATE_TIME));
            
            // Save and return
            return doubtRepository.save(doubt);
        }
        
        return null;
    }
    
    private QueryResponseDTO createQueryResponseDTO(Page<Doubt> doubtsPage, int page, int limit) {
        QueryResponseDTO responseDTO = new QueryResponseDTO();
        QueryResponseDTO.QueryDataDTO dataDTO = new QueryResponseDTO.QueryDataDTO();
        
        List<QueryResponseDTO.QueryDTO> queryDTOs = convertDoubtsToQueryDTOs(doubtsPage.getContent());
        
        dataDTO.setQueries(queryDTOs);
        dataDTO.setTotal((int) doubtsPage.getTotalElements());
        dataDTO.setPage(page);
        dataDTO.setLimit(limit);
        
        responseDTO.setData(dataDTO);
        
        return responseDTO;
    }
    
    private List<QueryResponseDTO.QueryDTO> convertDoubtsToQueryDTOs(List<Doubt> doubts) {
        List<QueryResponseDTO.QueryDTO> queryDTOs = new ArrayList<>();
        
        for (Doubt doubt : doubts) {
            QueryResponseDTO.QueryDTO dto = new QueryResponseDTO.QueryDTO();
            dto.setId(doubt.getId());
            
            // Set title (use title if available, otherwise use doubt field)
            dto.setTitle(doubt.getTitle() != null ? doubt.getTitle() : doubt.getDoubt());
            
            // Set body (use description if available)
            dto.setBody(doubt.getDescription());
            
            // Set code snippet
            dto.setCodeSnippet(doubt.getCodeSnippet());
            
            // Set author information
            Student student = doubt.getStudent();
            if (student != null) {
                QueryResponseDTO.AuthorDTO authorDTO = new QueryResponseDTO.AuthorDTO();
                authorDTO.setId(student.getId());
                authorDTO.setName(student.getName());
                authorDTO.setAvatar(student.getProfileImage() != null ? student.getProfileImage() : "/placeholder.svg");
                
                // Set role
                String role = student.getRole() == USER_ROLE.ROLE_TEACHER ? "Teacher" : "Student";
                authorDTO.setRole(role);
                
                dto.setAuthor(authorDTO);
            } else {
                // Create a placeholder author if student is null
                QueryResponseDTO.AuthorDTO authorDTO = new QueryResponseDTO.AuthorDTO();
                authorDTO.setId(0L);
                authorDTO.setName("Unknown User");
                authorDTO.setAvatar("/placeholder.svg");
                authorDTO.setRole("Student");
                dto.setAuthor(authorDTO);
            }
            
            // Set category from topic
            dto.setCategory(doubt.getTopic() != null ? doubt.getTopic() : "General");
            
            // Set tags from tags list
            dto.setTags(doubt.getTagsList());
            
            // Format date
            dto.setDate(doubt.getTimeSubmitted() != null ? 
                     doubt.getTimeSubmitted() : 
                     "2024-01-01");
            
            // Set views (random placeholder for now)
            dto.setViews(random.nextInt(200) + 50);
            
            // Set answers count
            int answersCount = answerRepository.countAnswersForDoubt(doubt.getId());
            dto.setAnswers(answersCount);
            
            // Set votes (random placeholder for now)
            dto.setVotes(random.nextInt(20));
            
            // Set status based on IsSolvedDoubt enum
            String status;
            if (doubt.getIsSolved() == IsSolvedDoubt.DONE) {
                status = "Answered";
            } else if (doubt.getIsSolved() == IsSolvedDoubt.IN_PROGRESS) {
                status = "In Progress";
            } else {
                status = "Open";
            }
            dto.setStatus(status);
            
            queryDTOs.add(dto);
        }
        
        return queryDTOs;
    }
} 