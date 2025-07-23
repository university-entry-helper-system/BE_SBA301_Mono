package com.example.SBA_M.service;

import com.example.SBA_M.dto.request.ScholarshipSearchRequest;
import com.example.SBA_M.entity.commands.Scholarship;

import java.util.List;

public interface ScholarshipService {
    Scholarship create(Scholarship scholarship, List<Integer> universityIds);
    Scholarship update(Integer id, Scholarship updatedScholarship, List<Integer> universityIds);
    void delete(Integer id);
    Scholarship getById(Integer id);
    List<Scholarship> getAll();
    List<Scholarship> search(ScholarshipSearchRequest request);

    List<Scholarship> getByUniversity(Integer universityId);
    List<Scholarship> getByEligibilityType(Scholarship.EligibilityType type);
    List<Scholarship> getByValueType(Scholarship.ValueType type);
}

