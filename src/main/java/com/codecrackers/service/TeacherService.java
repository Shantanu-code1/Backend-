package com.codecrackers.service;

import com.codecrackers.model.AvailableForDoubts;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.IsSolvedDoubt;

import java.util.List;

public interface TeacherService {
    // rating, list of submitted doubts, doubt response is done or not ?, teacher available or not,

    public Long rating(String jwt) throws Exception;
    public String isDoubtDoneOrNot(Long id, IsSolvedDoubt isSolvedDoubt);
    public List<Doubt> listOfSubmittedDoubts(String jwt, IsSolvedDoubt isSolvedDoubt) throws Exception;
    public String teacherOfflineOrNot(String jwt, AvailableForDoubts availableForDoubts) throws Exception;
}
