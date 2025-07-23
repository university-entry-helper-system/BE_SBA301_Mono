package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.ScholarshipSearchRequest;
import com.example.SBA_M.entity.commands.Scholarship;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.repository.commands.ScholarshipRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.ScholarshipService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final UniversityRepository universityRepository;

    @Override
    public Scholarship create(Scholarship scholarship, List<Integer> universityIds) {
        if (universityIds != null && !universityIds.isEmpty()) {
            List<University> universities = universityRepository.findAllById(universityIds);
            scholarship.setUniversities(universities);
        } else {
            scholarship.setUniversities(Collections.emptyList());
        }
        return scholarshipRepository.save(scholarship);
    }

    @Override
    public Scholarship update(Integer id, Scholarship updatedScholarship, List<Integer> universityIds) {
        Scholarship existing = scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        existing.setName(updatedScholarship.getName());
        existing.setDescription(updatedScholarship.getDescription());
        existing.setValueType(updatedScholarship.getValueType());
        existing.setValueAmount(updatedScholarship.getValueAmount());
        existing.setEligibilityType(updatedScholarship.getEligibilityType());
        existing.setMinScore(updatedScholarship.getMinScore());
        existing.setApplyLink(updatedScholarship.getApplyLink());
        existing.setApplicationDeadline(updatedScholarship.getApplicationDeadline());

        List<University> universities = universityRepository.findAllById(universityIds);
        existing.setUniversities(universities);

        return scholarshipRepository.save(existing);
    }


    @Override
    public List<Scholarship> search(ScholarshipSearchRequest request) {
        return scholarshipRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null && !request.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }

            if (request.getValueType() != null) {
                predicates.add(cb.equal(root.get("valueType"), request.getValueType()));
            }

            if (request.getEligibilityType() != null) {
                predicates.add(cb.equal(root.get("eligibilityType"), request.getEligibilityType()));
            }

            if ("ACTIVE".equalsIgnoreCase(request.getStatus())) {
                predicates.add(cb.greaterThan(root.get("applicationDeadline"), java.time.LocalDate.now()));
            } else if ("EXPIRED".equalsIgnoreCase(request.getStatus())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("applicationDeadline"), java.time.LocalDate.now()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }




    @Override
    public void delete(Integer id) {
        scholarshipRepository.deleteById(id);
    }

    @Override
    public Scholarship getById(Integer id) {
        return scholarshipRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Scholarship> getAll() {
        return scholarshipRepository.findAll();
    }

    @Override
    public List<Scholarship> getByUniversity(Integer universityId) {
        return scholarshipRepository.findByUniversityId(universityId);
    }

    @Override
    public List<Scholarship> getByEligibilityType(Scholarship.EligibilityType type) {
        return scholarshipRepository.findByEligibilityType(type);
    }

    @Override
    public List<Scholarship> getByValueType(Scholarship.ValueType type) {
        return scholarshipRepository.findByValueType(type);
    }
}

