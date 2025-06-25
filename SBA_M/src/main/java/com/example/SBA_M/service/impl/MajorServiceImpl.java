package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.MajorRequest;
import com.example.SBA_M.dto.response.MajorParentResponse;
import com.example.SBA_M.dto.response.MajorResponse;
import com.example.SBA_M.dto.response.SubjectCombinationResponse;
import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.entity.commands.SubjectCombination;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.MajorRepository;
import com.example.SBA_M.repository.commands.SubjectCombinationRepostiory;
import com.example.SBA_M.service.MajorService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final SubjectCombinationRepostiory subjectCombinationRepository;

    @Override
    @Transactional
    public MajorResponse createMajor(MajorRequest request) {
        log.info("Creating new major with name: {}", request.getName());

        List<SubjectCombination> subjectCombinations = List.of();
        if (request.getSubjectCombinationIds() != null && !request.getSubjectCombinationIds().isEmpty()) {
            subjectCombinations = subjectCombinationRepository.findAllById(request.getSubjectCombinationIds());
            if (subjectCombinations.size() != request.getSubjectCombinationIds().size()) {
                throw new AppException(ErrorCode.SUBJECT_COMBINATION_NOT_FOUND);
            }
        }

        Major major = new Major();
        major.setName(request.getName());
        major.setCode(request.getCode());
        major.setDegree(request.getDegree());
        major.setDescription(request.getDescription());
        major.setSubjectCombinations(subjectCombinations);
        major.setStatus(Status.ACTIVE);
        major = majorRepository.save(major);
        log.info("Major created with ID: {}", major.getId());
        return mapToResponse(major);
    }

    @Override
    @Transactional(readOnly = true)
    public MajorResponse getMajorById(Long id) {
        log.info("Fetching major with ID: {}", id);
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        return mapToResponse(major);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MajorResponse> getAllMajors() {
        log.info("Fetching all majors");
        return majorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MajorResponse updateMajor(Long id, MajorRequest request) {
        log.info("Updating major with ID: {}", id);
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        List<SubjectCombination> subjectCombinations = List.of();
        if (request.getSubjectCombinationIds() != null && !request.getSubjectCombinationIds().isEmpty()) {
            subjectCombinations = subjectCombinationRepository.findAllById(request.getSubjectCombinationIds());
            if (subjectCombinations.size() != request.getSubjectCombinationIds().size()) {
                throw new AppException(ErrorCode.SUBJECT_COMBINATION_NOT_FOUND);
            }
        }

        major.setName(request.getName());
        major.setCode(request.getCode());
        major.setDegree(request.getDegree());
        major.setDescription(request.getDescription());
        major.setSubjectCombinations(subjectCombinations);

        major = majorRepository.save(major);
        log.info("Major updated with ID: {}", major.getId());
        return mapToResponse(major);
    }

    @Override
    @Transactional
    public void deleteMajor(Long id) {
        log.info("Deleting major with ID: {}", id);
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        major.setStatus(Status.DELETED);
        majorRepository.save(major);
        log.info("Major deleted with ID: {}", id);
    }

    private MajorResponse mapToResponse(Major major) {
        return MajorResponse.builder()
                .id(major.getId())
                .name(major.getName())
                .code(major.getCode())
                .degree(major.getDegree())
                .description(major.getDescription())
                .subjectCombinations(major.getSubjectCombinations() != null ?
                        major.getSubjectCombinations().stream()
                                .map(sc -> SubjectCombinationResponse.builder()
                                        .id(sc.getId())
                                        .name(sc.getName())
                                        .build())
                                .collect(Collectors.toList()) : List.of())
                .build();
    }
}