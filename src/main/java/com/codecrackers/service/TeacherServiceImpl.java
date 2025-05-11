package com.codecrackers.service;

import com.codecrackers.model.*;
import com.codecrackers.repository.DoubtRepository;
import com.codecrackers.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    private StudentService studentService;

    @Autowired
    private DoubtRepository doubtRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Long rating(String jwt) throws Exception {
        Student student = studentService.findUserByJWT(jwt);

        if(student == null){
            throw new Exception("Student not found");
        }

        List<Review> reviews = reviewRepository.findByTeacherEmail(student.getEmail());

        Long totalRating = 0L;
        for(int i=0; i<reviews.size(); i++){
            totalRating += reviews.get(i).getRating();
        }

        Long finalRating = totalRating/reviews.size();

        return finalRating;
    }

    @Override
    public String isDoubtDoneOrNot(Long id, IsSolvedDoubt isSolvedDoubt) {
        Optional<Doubt> doubt = doubtRepository.findById(id);

        if(doubt.isPresent()){
            doubt.get().setIsSolved(isSolvedDoubt);
            doubtRepository.save(doubt.get());
            return "Doubt status updated";
        }

        return "Getting Issues while updated doubt status";
    }

    @Override
    public List<Doubt> listOfSubmittedDoubts(String jwt, IsSolvedDoubt isSolvedDoubt) throws Exception {
        Student student = studentService.findUserByJWT(jwt);

        if(student == null){
            throw new Exception("Student not found");
        }

        return doubtRepository.findByIsSolved(isSolvedDoubt);
    }

    @Override
    public String teacherOfflineOrNot(String jwt, AvailableForDoubts availableForDoubts) throws Exception {
        Student student = studentService.findUserByJWT(jwt);

        if(student == null){
            throw new Exception("Student not found");
        }

        student.setAvailableForDoubts(availableForDoubts);

        return "UPDATED";
    }
}
