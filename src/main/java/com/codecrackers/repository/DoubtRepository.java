package com.codecrackers.repository;

import com.codecrackers.model.Doubt;
import com.codecrackers.model.IsSolvedDoubt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
    List<Doubt> findByStudentId(Long id);
    List<Doubt> findByIsSolved(IsSolvedDoubt isSolvedDoubt);
    
    // Find recent doubts
    @Query("SELECT d FROM Doubt d ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubts();
    
    // Find recent doubts with limit
    @Query(value = "SELECT * FROM doubts ORDER BY time_submitted DESC LIMIT :limit", nativeQuery = true)
    List<Doubt> findRecentDoubtsWithLimit(@Param("limit") int limit);
    
    // Find recent doubts by category
    @Query("SELECT d FROM Doubt d WHERE d.topic = :category ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubtsByCategory(@Param("category") String category);
    
    // TODO: Implement this when the doubt_replies table is created
    // Count replies for a doubt 
    // @Query(value = "SELECT COUNT(*) FROM doubt_replies WHERE doubt_id = :doubtId", nativeQuery = true)
    // int countRepliesForDoubt(@Param("doubtId") Long doubtId);
}
