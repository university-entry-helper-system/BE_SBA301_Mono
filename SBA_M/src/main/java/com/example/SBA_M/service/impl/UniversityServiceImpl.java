package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.UniversityService;
import com.example.SBA_M.service.messaging.producer.UniversityProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.SBA_M.dto.response.PageResponse;


@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityRepository universityRepository;
    private final UniversityProducer universityProducer;
    private final UniversityReadRepository universityReadRepository;


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
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityDocument getUniversityById(Integer id) {
        return universityReadRepository.findById(id).orElseThrow(() -> new RuntimeException("University not found with id: " + id));
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityResponse createUniversity(UniversityRequest university) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();
            return universityProducer.createUniversity(university, username);
        } catch (AuthenticationException | AppException ex) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityResponse updateUniversity(Integer id, UniversityRequest universityRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();
            return universityProducer.updateUniversity(id, universityRequest, username);
        } catch (AuthenticationException | AppException ex) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        } catch (RuntimeException ex) {
            throw new AppException(ErrorCode.UNIVERSITY_NOT_FOUND);
        }
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUniversity(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();
            universityProducer.deleteUniversity(id, username);
        } catch (AuthenticationException | AppException ex) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        } catch (RuntimeException ex) {
            throw new AppException(ErrorCode.UNIVERSITY_NOT_FOUND);
        }
    }
}