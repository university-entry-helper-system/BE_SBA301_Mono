package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityMethodRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityAdmissionMethodResponse;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.UniversityAdmissionMethodMapper;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.repository.commands.UniversityAdmissionMethodRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.UniversityAdmissionMethodService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityAdmissionMethodServiceImpl implements UniversityAdmissionMethodService {
    private final UniversityAdmissionMethodRepository universityAdmissionMethodRepository;
    private final UniversityRepository universityRepository;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final UniversityAdmissionMethodMapper mapper;

    @Override
    public PageResponse<UniversityAdmissionMethodResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UniversityAdmissionMethod> methodPage = universityAdmissionMethodRepository.findByStatus(Status.ACTIVE, pageable);
        List<UniversityAdmissionMethodResponse> items = methodPage.getContent().stream()
                .map(mapper::toResponse)
                .toList();

        return PageResponse.<UniversityAdmissionMethodResponse>builder()
                .page(methodPage.getNumber())
                .size(methodPage.getSize())
                .totalElements(methodPage.getTotalElements())
                .totalPages(methodPage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    public UniversityAdmissionMethodResponse getById(Integer id) {
        UniversityAdmissionMethod uam = universityAdmissionMethodRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        return mapper.toResponse(uam);
    }

    @Override
    @Transactional
    public UniversityAdmissionMethodResponse create(UniversityMethodRequest request) {
        University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        AdmissionMethod method = admissionMethodRepository.findByIdAndStatus(request.getAdmissionMethodId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PARAM, "Admission method not found"));

        UniversityAdmissionMethod uam = new UniversityAdmissionMethod();
        uam.setUniversity(university);
        uam.setAdmissionMethod(method);
        uam.setYear(request.getYear());
        uam.setNotes(request.getNotes());
        uam.setConditions(request.getConditions());
        uam.setRegulations(request.getRegulations());
        uam.setAdmissionTime(request.getAdmissionTime());

        UniversityAdmissionMethod saved = universityAdmissionMethodRepository.save(uam);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UniversityAdmissionMethodResponse update(Integer id, UniversityMethodRequest request) {
        UniversityAdmissionMethod uam = universityAdmissionMethodRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        if (request.getUniversityId() != null) {
            University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
            uam.setUniversity(university);
        }

        if (request.getAdmissionMethodId() != null) {
            AdmissionMethod method = admissionMethodRepository.findByIdAndStatus(request.getAdmissionMethodId(), Status.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_PARAM, "Admission method not found"));
            uam.setAdmissionMethod(method);
        }

        uam.setYear(request.getYear());
        uam.setNotes(request.getNotes());
        uam.setConditions(request.getConditions());
        uam.setRegulations(request.getRegulations());
        uam.setAdmissionTime(request.getAdmissionTime());

        UniversityAdmissionMethod saved = universityAdmissionMethodRepository.save(uam);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        UniversityAdmissionMethod uam = universityAdmissionMethodRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        uam.setStatus(Status.DELETED);
        universityAdmissionMethodRepository.save(uam);
    }
}
