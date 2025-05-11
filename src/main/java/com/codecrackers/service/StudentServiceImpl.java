package com.codecrackers.service;

import com.codecrackers.config.JwtProvider;
import com.codecrackers.dto.DoubtRequestDTO;
import com.codecrackers.model.*;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.repository.StudentRepository;
import com.codecrackers.request.ReviewRequest;
import com.codecrackers.response.ProfileResponseStudent;
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

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("No user found with email: " + email));
        
        return student;
    }

    @Override
    public Student findUserByEmail(String username) throws Exception {
        Student student = studentRepository.findByEmail(username)
                .orElseThrow(() -> new Exception("No user found with email: " + username));
        
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
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student with email " + email + " not found."));

        if(student.getPoints() == null || student.getPoints() == 0L){
            student.setPoints(1L);
        }
        else {
            student.setPoints(student.getPoints() + 1);
        }

        studentRepository.save(student);
    }

    @Override
    public boolean freeDoubtAvailable(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student with email " + email + " not found."));
        return student.getPoints() > 3;
    }

    @Override
    public void addYourReviewAboutTeacher(ReviewRequest review) {
        Student student = studentRepository.findByEmail(review.getEmail())
                .orElse(null);

        if (student == null) {
            System.out.println("Student not found for review: " + review.getEmail());
            return;
        }

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
        
        Doubt doubt = new Doubt();
        doubt.setTitle(doubtRequestDTO.getTitle());
        doubt.setTopic(doubtRequestDTO.getCategory());
        doubt.setDescription(doubtRequestDTO.getDescription());
        doubt.setCodeSnippet(doubtRequestDTO.getCode());
        doubt.setTagsFromList(doubtRequestDTO.getTags());
        
        doubt.setStudent(student);
        doubt.setIsSolved(IsSolvedDoubt.PENDING);
        doubt.setTimeSubmitted(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        doubt.setDoubt(doubtRequestDTO.getTitle());
        
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
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Student not found with email: " + email));

        ObjectMapper objectMapper = new ObjectMapper();
        String object1AsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
        String object2AsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(anyQuery);

        String combinedMessage = "Object 1:\n" + object1AsString + "\n\nObject 2:\n" + object2AsString;

        emailService.sendNotificationEmail("Resolve Query", combinedMessage);
    }

}
