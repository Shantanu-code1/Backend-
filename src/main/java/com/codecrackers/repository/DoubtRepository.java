package com.codecrackers.repository;

import com.codecrackers.model.Doubt;
import com.codecrackers.model.IsSolvedDoubt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
    List<Doubt> findByStudentId(Long id);
    List<Doubt> findByIsSolved(IsSolvedDoubt isSolvedDoubt);
}
