package com.codecrackers.service;

import com.codecrackers.config.JwtProvider;
import com.codecrackers.dto.DoubtRequestDTO;
import com.codecrackers.model.*;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.repository.StudentRepository;
import com.codecrackers.request.ReviewRequest;
import com.codecrackers.response.ProfileResponseStudent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DoubtRepository doubtRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmailService emailService;

    @Override
    public Student findUserByJWT(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        Student student = studentRepository.findByEmail(email);
        if(student == null){
            throw new Exception("No user found");
        }
        return student;
    }

    @Override
    public Student findUserByEmail(String username) throws Exception {
        Student student = studentRepository.findByEmail(username);
        if(student == null){
            throw new Exception("No user found");
        }
        return student;
    }

    @Override
    public void updateStudentProfile(ProfileResponseStudent profileResponseStudent, Long id) throws Exception {
        Optional<Student> student = studentRepository.findById(id);

        if (student.isPresent()) {
            Student studentUpdate = student.get();
            studentUpdate.setProfileImage(profileResponseStudent.getProfileImage());
            studentUpdate.setName(profileResponseStudent.getName());
            studentUpdate.setCodingProfileLink(profileResponseStudent.getCodingProfileLink());
            studentUpdate.setPhoneNumber(profileResponseStudent.getPhoneNumber());

            studentRepository.save(studentUpdate);
        } else {
            throw new Exception("Student not found with this id" + id);
        }

    }

    @Override
    public List<Doubt> listAllDoubtsOfStudents(Long id) {
        return doubtRepository.findByStudentId(id);
    }

    @Override
    public void increasePointOnShare(String email) {
        Student student = studentRepository.findByEmail(email);

        if (student != null) {
            // Increment the points by getting current points, adding 1, and setting it
            if(student.getPoints() == null || student.getPoints() == 0L){
                student.setPoints(1L);
            }
            else {
                student.setPoints(student.getPoints() + 1);
            }

            // Save the updated student object back to the repository
            studentRepository.save(student);
        } else {
            // Handle the case where the student is not found
            throw new RuntimeException("Student with email " + email + " not found.");
        }
    }

    @Override
    public boolean freeDoubtAvailable(String email) {
        Student student = studentRepository.findByEmail(email);
        return student.getPoints() > 3;
    }

    @Override
    public void addYourReviewAboutTeacher(ReviewRequest review) {
        // Fetch the student by email
        Student student = studentRepository.findByEmail(review.getEmail());

        // Check if student exists
        if (student == null) {
            return;
        }

        // Initialize review list if null and add the new review
        if (student.getReview() == null) {
            student.setReview(new ArrayList<>());
        }
        student.getReview().add(review.getReview());

        studentRepository.save(student);

    }

    @Override
    public void submitDoubt(String jwt, Doubt doubt) throws Exception {
        Student student = findUserByJWT(jwt);

        if(student == null){
            throw new Exception("Log in or register");
        }

        if (student.getDoubts() == null) {
            student.setDoubts(new ArrayList<>());
        }
        doubtRepository.save(doubt);
        student.getDoubts().add(doubt);

        studentRepository.save(student);
    }

    @Override
    public Doubt submitDoubtFromDTO(String jwt, DoubtRequestDTO doubtRequestDTO) throws Exception {
        Student student = findUserByJWT(jwt);

        if(student == null){
            throw new Exception("Log in or register");
        }
        
        // Create a new doubt from the DTO
        Doubt doubt = new Doubt();
        doubt.setTitle(doubtRequestDTO.getTitle());
        doubt.setTopic(doubtRequestDTO.getCategory());
        doubt.setDescription(doubtRequestDTO.getDescription());
        doubt.setCodeSnippet(doubtRequestDTO.getCode());
        doubt.setTagsFromList(doubtRequestDTO.getTags());
        
        // Set additional fields
        doubt.setStudent(student);
        doubt.setIsSolved(IsSolvedDoubt.PENDING);
        doubt.setTimeSubmitted(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // For backward compatibility, set the doubt field to match title
        doubt.setDoubt(doubtRequestDTO.getTitle());
        
        // Save the doubt and associate with student
        if (student.getDoubts() == null) {
            student.setDoubts(new ArrayList<>());
        }
        
        Doubt savedDoubt = doubtRepository.save(doubt);
        student.getDoubts().add(savedDoubt);
        studentRepository.save(student);
        
        return savedDoubt;
    }

    @Override
    public List<Doubt> allDoubtsList(Long id) {
        List<Doubt> doubts = doubtRepository.findByStudentId(id);
        return doubts != null ? doubts : Collections.emptyList();
    }

    @Override
    public String giveReferralToSomeone(String jwt) throws Exception {

        Student student = findUserByJWT(jwt);

        if(student == null){
            throw new Exception("No student found with this email");
        }

        String url = "http://localhost:3000?ref";
        url += student.getEmail();

        return url;
    }

    @Override
    public List<Student> allTeacherList() {
        return studentRepository.findByRole(USER_ROLE.ROLE_TEACHER);
    }

    @Override
    public void sendQueryToAdminMeansMe(AnyQuery anyQuery, String email) throws Exception {
        Student student = studentRepository.findByEmail(email);

        if(student == null){
            throw new Exception("Student not found");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String object1AsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        String object2AsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyQuery);

        String combinedMessage = "Object 1:\n" + object1AsString + "\n\nObject 2:\n" + object2AsString;

        emailService.sendNotificationEmail("Resolve Query", combinedMessage);
    }

}
