package com.codecrackers.service;

import com.codecrackers.dto.DoubtRequestDTO;
import com.codecrackers.model.AnyQuery;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.Review;
import com.codecrackers.model.Student;
import com.codecrackers.request.ReviewRequest;
import com.codecrackers.response.ProfileResponseStudent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface StudentService {
    public Student findUserByJWT(String username) throws Exception;

    public Student findUserByEmail(String username) throws Exception;

    public void updateStudentProfile(ProfileResponseStudent profileResponseStudent, Long id) throws Exception;

    public List<Doubt> listAllDoubtsOfStudents(Long id);

    public void increasePointOnShare(String email);

    public boolean freeDoubtAvailable(String email);

    public void addYourReviewAboutTeacher(ReviewRequest reviewRequest);

    public void submitDoubt(String jwt, Doubt doubt) throws Exception;

    public Doubt submitDoubtFromDTO(String jwt, DoubtRequestDTO doubtRequestDTO) throws Exception;

    public List<Doubt> allDoubtsList(Long id) throws Exception;

    public String giveReferralToSomeone(String jwt) throws Exception;

    public List<Student> allTeacherList();

    public void sendQueryToAdminMeansMe(AnyQuery anyQuery, String email) throws Exception;
}