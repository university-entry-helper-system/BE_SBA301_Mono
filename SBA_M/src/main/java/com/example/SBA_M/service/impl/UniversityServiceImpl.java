package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.UniversityMapper;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import com.example.SBA_M.service.UniversityService;
import com.example.SBA_M.service.messaging.producer.UniversityProducer;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityReadRepository universityReadRepository;
    private final UniversityProducer universityProducer;
    private final UniversityMapper universityMapper;

    @Override
    public PageResponse<University> getAllUniversities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<University> universityPage = universityRepository.findAll(pageable);
        return PageResponse.<University>builder()
                .page(universityPage.getNumber())
                .size(universityPage.getSize())
                .totalElements(universityPage.getTotalElements())
                .totalPages(universityPage.getTotalPages())
                .items(universityPage.getContent())
                .build();
    }

    @Override
    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    @Override
    public UniversityDocument getUniversityById(Integer id) {
        return universityReadRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
    }

    @Override
    public UniversityResponse createUniversity(UniversityRequest universityRequest) {
        String username = getCurrentUsername();

        University university = universityMapper.toEntity(universityRequest, username);
        University saved = universityRepository.save(university);

        universityProducer.sendUniversityCreated(saved);
        return universityMapper.toResponse(saved);
    }

    @Override
    public UniversityResponse updateUniversity(Integer id, UniversityRequest universityRequest) {
        String username = getCurrentUsername();

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        universityMapper.updateEntityFromRequest(universityRequest, university, username);
        university.setUpdatedAt(Instant.now());
        University updated = universityRepository.save(university);

        universityProducer.sendUniversityUpdated(updated);
        return universityMapper.toResponse(updated);
    }

    @Override
    public void deleteUniversity(Integer id) {
        String username = getCurrentUsername();

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        university.setStatus(Status.DELETED);
        university.setUpdatedBy(username);
        university.setUpdatedAt(Instant.now());
        University deleted = universityRepository.save(university);

        universityProducer.sendUniversityDeleted(deleted);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return authentication.getName();
    }
}
