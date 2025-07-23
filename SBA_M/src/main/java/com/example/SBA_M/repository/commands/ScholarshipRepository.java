package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Scholarship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Integer>, JpaSpecificationExecutor<Scholarship> {

    @Query("SELECT s FROM Scholarship s JOIN s.universities u WHERE u.id = :universityId")
    List<Scholarship> findByUniversityId(@Param("universityId") Integer universityId);

    List<Scholarship> findByEligibilityType(Scholarship.EligibilityType eligibilityType);

    List<Scholarship> findByValueType(Scholarship.ValueType valueType);
}