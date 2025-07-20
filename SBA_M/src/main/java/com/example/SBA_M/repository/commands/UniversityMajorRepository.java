package com.example.SBA_M.repository.commands;

import com.example.SBA_M.dto.response.ComboCountProjection;
import com.example.SBA_M.entity.commands.UniversityMajor;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityMajorRepository extends JpaRepository<UniversityMajor, Integer> {
    Optional<UniversityMajor> findByIdAndStatus(Integer id, Status status);

    Page<UniversityMajor> findByStatus(Status status, Pageable pageable);



    // Count how many university majors exist by university and subject combination
    @Query("SELECT sc.id AS comboId, COUNT(um.id) AS count " +
            "FROM UniversityMajor um " +
            "JOIN um.subjectCombinations sc " +
            "WHERE um.university.id = :universityId " +
            "AND sc.id IN :comboIds " +
            "AND um.year = :year " +
            "AND um.status = :status " +
            "GROUP BY sc.id")
    List<ComboCountProjection> countByUniversityIdAndSubjectCombinationIdsAndYear(
            @Param("universityId") Integer universityId,
            @Param("comboIds") List<Long> comboIds,
            @Param("status") Status status,
            @Param("year") Integer year
    );

    int countByUniversityIdAndMajorIdAndStatusAndYear(Integer universityId, Long majorId, Status status, Integer year);
}


