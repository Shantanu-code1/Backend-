package com.codecrackers.repository;

import com.codecrackers.model.AvailableForDoubts;
import com.codecrackers.model.Student;
import com.codecrackers.model.USER_ROLE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByEmail(String username);
    List<Student> findByRole(USER_ROLE role);
    List<Student> findByAvailableForDoubts(AvailableForDoubts availableForDoubts);
}
