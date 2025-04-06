package com.codecrackers.controller;

import com.codecrackers.dto.DoubtRequestDTO;
import com.codecrackers.model.AnyQuery;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.Review;
import com.codecrackers.model.Student;
import com.codecrackers.repository.ReviewRepository;
import com.codecrackers.repository.StudentRepository;
import com.codecrackers.request.ReviewRequest;
import com.codecrackers.response.ProfileResponseStudent;
import com.codecrackers.service.StudentService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private StudentService studentService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseStudent> profile(@RequestHeader("Authorization") String jwt) throws Exception {
        Student student = studentService.findUserByJWT(jwt);

        ProfileResponseStudent profileResponseStudent = new ProfileResponseStudent();

        profileResponseStudent.setProfileImage(student.getProfileImage());
        profileResponseStudent.setName(student.getName());
        profileResponseStudent.setEmail(student.getEmail());
        profileResponseStudent.setCodingProfileLink(student.getCodingProfileLink());
        profileResponseStudent.setPhoneNumber(student.getPhoneNumber());
        profileResponseStudent.setPoints(student.getPoints());
        profileResponseStudent.setShares(student.getShares());
        profileResponseStudent.setDoubtFree(student.isDoubtFree());

        return new ResponseEntity<>(profileResponseStudent, HttpStatus.OK);
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<String> updateProfile(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long id, @RequestBody ProfileResponseStudent profileResponseStudent) throws Exception {
        studentService.updateStudentProfile(profileResponseStudent, id);
        return new ResponseEntity<>("Updated Profile", HttpStatus.OK);
    }

    @GetMapping("/profile/doubts/{id}")
    public ResponseEntity<List<Doubt>> doubtHistory(@PathVariable("id") Long id){
        List<Doubt> doubts = studentService.listAllDoubtsOfStudents(id);
        return new ResponseEntity<>(doubts, HttpStatus.OK);
    }

    @GetMapping("/profile/doubts/free")
    public ResponseEntity<Boolean> isDoubtFreeAvailable(@RequestBody String email){
        boolean isFree = studentService.freeDoubtAvailable(email);
        return new ResponseEntity<>(isFree, HttpStatus.OK);
    }

    @PutMapping("/profile/doubts/manually/free")
    public ResponseEntity<Void> updateDoubtFreeForManually(@RequestBody String email) throws Exception {
        Student student = studentService.findUserByEmail(email);
        if(student == null){
            throw new Exception("User not found with this email");
        }

        student.setDoubtFree(true);
        studentRepository.save(student);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/add-review")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest) {
        Student student = reviewRequest.getReview().getStudent();

        // Check if the student is new or already exists in the database
        if (student.getId() == null || studentRepository.findByEmail(student.getEmail()) != null) {
            student = studentRepository.save(student); // Save the student first
        }

        reviewRequest.getReview().setStudent(student);
        reviewRepository.save(reviewRequest.getReview()); // Save the review with the persisted student

        return new ResponseEntity<>("Review done successfully", HttpStatus.CREATED);
    }

    @PostMapping("/doubt")
    public ResponseEntity<String> submitDoubt(@RequestHeader("Authorization") String jwt, Doubt doubt) throws Exception {
        studentService.submitDoubt(jwt, doubt);
        return new ResponseEntity<>("Submitted", HttpStatus.OK);
    }

    @PostMapping("/submit-question")
    public ResponseEntity<?> submitQuestion(
            @RequestHeader("Authorization") String jwt, 
            @RequestBody DoubtRequestDTO doubtRequestDTO) throws Exception {
        try {
            // Validate required fields
            if (doubtRequestDTO.getTitle() == null || doubtRequestDTO.getTitle().trim().isEmpty()) {
                return new ResponseEntity<>(
                    Map.of("error", "Question title is required"), 
                    HttpStatus.BAD_REQUEST
                );
            }
            
            if (doubtRequestDTO.getCategory() == null || doubtRequestDTO.getCategory().trim().isEmpty()) {
                return new ResponseEntity<>(
                    Map.of("error", "Category is required"), 
                    HttpStatus.BAD_REQUEST
                );
            }
            
            if (doubtRequestDTO.getDescription() == null || doubtRequestDTO.getDescription().trim().isEmpty()) {
                return new ResponseEntity<>(
                    Map.of("error", "Description is required"), 
                    HttpStatus.BAD_REQUEST
                );
            }
            
            // Submit the doubt using our new service method
            Doubt savedDoubt = studentService.submitDoubtFromDTO(jwt, doubtRequestDTO);
            
            // Return success response with the created doubt
            return new ResponseEntity<>(
                Map.of(
                    "message", "Question submitted successfully",
                    "doubt", savedDoubt
                ), 
                HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                Map.of("error", e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/doubt/{id}")
    public ResponseEntity<List<Doubt>> listOfAllDoubts(@PathParam("id") Long id) throws Exception {
        return new ResponseEntity<>(studentService.allDoubtsList(id), HttpStatus.OK);
    }

    @PostMapping("/give-referral")
    public ResponseEntity<String> giveReferralToSomeone(@RequestHeader("Authorization") String jwt) throws Exception {
        String url = studentService.giveReferralToSomeone(jwt);
        if(Objects.equals(url, "")){
            return new ResponseEntity<>("No student found with this email or you are not able to give and referral", HttpStatus.OK);
        }
        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<Student>> getAllTeachersList(){
        return new ResponseEntity<>(studentService.allTeacherList(), HttpStatus.OK);
    }

    @PostMapping("/query")
    public ResponseEntity<String> sendingQueries(@RequestBody AnyQuery anyQuery, @RequestBody String email) throws Exception {
        studentService.sendQueryToAdminMeansMe(anyQuery, email);
        return new ResponseEntity<>("Email send successfully", HttpStatus.OK);
    }

}
