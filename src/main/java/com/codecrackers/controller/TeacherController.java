package com.codecrackers.controller;

import com.codecrackers.model.AvailableForDoubts;
import com.codecrackers.model.Doubt;
import com.codecrackers.model.IsSolvedDoubt;
import com.codecrackers.repository.StudentRepository;
import com.codecrackers.service.StudentService;
import com.codecrackers.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/ratings")
    ResponseEntity<Long> getRatingForThisTeacher(@RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(teacherService.rating(jwt), HttpStatus.OK);
    }

    @PostMapping("/doubt-status/{id}")
    ResponseEntity<String> postInfoAboutIsDoubtDoneOrNot(@PathVariable Long id, @RequestBody IsSolvedDoubt isSolvedDoubt) throws Exception {
        return new ResponseEntity<>(teacherService.isDoubtDoneOrNot(id, isSolvedDoubt), HttpStatus.CREATED);
    }

    @GetMapping("/doubts")
    ResponseEntity<List<Doubt>> getListOfSubmittedDoubts(@RequestHeader("Authorization") String jwt, @RequestBody IsSolvedDoubt isSolvedDoubt) throws Exception {
        List<Doubt> doubts = teacherService.listOfSubmittedDoubts(jwt, isSolvedDoubt);
        return new ResponseEntity<>(doubts, HttpStatus.OK);
    }

    @PostMapping("/update-status/online-offline")
    ResponseEntity<String> teacherOfflineOrOnline(@RequestHeader("Authorization") String jwt, @RequestBody AvailableForDoubts availableForDoubts) throws Exception {
        return new ResponseEntity<>(teacherService.teacherOfflineOrNot(jwt, availableForDoubts), HttpStatus.OK);
    }

}
