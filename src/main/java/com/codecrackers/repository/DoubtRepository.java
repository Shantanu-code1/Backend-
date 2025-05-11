package com.codecrackers.repository;

import com.codecrackers.model.Doubt;
import com.codecrackers.model.DoubtType;
import com.codecrackers.model.IsSolvedDoubt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {
    List<Doubt> findByStudentId(Long id);
    List<Doubt> findByIsSolved(IsSolvedDoubt isSolvedDoubt);
    
    // Find doubts by student email
    @Query("SELECT d FROM Doubt d WHERE d.student.email = :email")
    List<Doubt> findByStudentEmail(@Param("email") String email);
    
    // Find recent doubts
    @Query("SELECT d FROM Doubt d ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubts();
    
    // Find recent doubts with limit
    @Query(value = "SELECT * FROM doubts ORDER BY time_submitted DESC LIMIT :limit", nativeQuery = true)
    List<Doubt> findRecentDoubtsWithLimit(@Param("limit") int limit);
    
    // Find recent doubts by category
    @Query("SELECT d FROM Doubt d WHERE d.topic = :category ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubtsByCategory(@Param("category") String category);
    
    // Find doubts by type
    List<Doubt> findByType(DoubtType type);
    
    // Find recent doubts by type
    @Query("SELECT d FROM Doubt d WHERE d.type = :type ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubtsByType(@Param("type") DoubtType type);
    
    // Find recent doubts by type with limit
    @Query(value = "SELECT * FROM doubts WHERE type = :type ORDER BY time_submitted DESC LIMIT :limit", nativeQuery = true)
    List<Doubt> findRecentDoubtsByTypeWithLimit(@Param("type") String type, @Param("limit") int limit);
    
    // Find recent doubts by category and type
    @Query("SELECT d FROM Doubt d WHERE d.topic = :category AND d.type = :type ORDER BY d.timeSubmitted DESC")
    List<Doubt> findRecentDoubtsByCategoryAndType(@Param("category") String category, @Param("type") DoubtType type);
    
    // Find paginated doubts by type
    Page<Doubt> findByType(DoubtType type, Pageable pageable);
    
    // Count replies for a doubt 
    // @Query(value = "SELECT COUNT(*) FROM doubt_replies WHERE doubt_id = :doubtId", nativeQuery = true)
    // int countRepliesForDoubt(@Param("doubtId") Long doubtId);
}
