package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectCombinationRepository extends JpaRepository<SubjectCombination, Long> {

    Page<SubjectCombination> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);

    // Tạm thời sử dụng method đơn giản
    Page<SubjectCombination> findByStatus(Status status, Pageable pageable);
    
    @Query("SELECT sc FROM SubjectCombination sc " +
           "WHERE (:status IS NULL OR sc.status = :status) " +
           "AND (:name IS NULL OR LOWER(sc.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<SubjectCombination> searchSubjectCombinations(@Param("status") Status status,
                                                      @Param("name") String name,
                                                      @Param("blockName") String blockName,
                                                      @Param("examSubjectName") String examSubjectName,
                                                      Pageable pageable);
}
