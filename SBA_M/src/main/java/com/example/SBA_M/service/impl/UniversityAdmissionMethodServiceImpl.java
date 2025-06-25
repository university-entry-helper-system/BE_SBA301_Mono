package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityMethodRequest;
import com.example.SBA_M.dto.response.*;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import com.example.SBA_M.entity.queries.UniversityEntriesDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.UniversityAdmissionMethodMapper;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.repository.commands.UniversityAdmissionMethodRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.repository.queries.UniversityAdmissionMethodReadRepository;
import com.example.SBA_M.service.UniversityAdmissionMethodService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityAdmissionMethodServiceImpl implements UniversityAdmissionMethodService {
    private final UniversityAdmissionMethodRepository universityAdmissionMethodRepository;
    private final UniversityRepository universityRepository;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final UniversityAdmissionMethodMapper mapper;
    private final UniversityAdmissionMethodReadRepository universityAdmissionMethodReadRepository;

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

    // Method 1: Get schools by method (latest year)
    @Override
    public List<UniversityAdmissionMethodSummaryResponse> getSchoolsByMethod(Integer methodId) {
        int currentYear = Year.now().getValue() + 1;
        final int MIN_VALID_YEAR = 2015;

        List<UniversityEntriesDocument> entries = List.of();

        while (currentYear >= MIN_VALID_YEAR) {
            entries = universityAdmissionMethodReadRepository
                    .findByMethodIdAndYearAndStatus(methodId, currentYear, Status.ACTIVE);
            if (!entries.isEmpty()) break;
            currentYear--;
        }

        if (entries.isEmpty()) return List.of();

        // Group by university and return summary
        Map<Integer, UniversityEntriesDocument> latestByUniversity = entries.stream()
                .collect(Collectors.toMap(
                        UniversityEntriesDocument::getUniversityId,
                        Function.identity(),
                        (e1, e2) -> e1.getYear() > e2.getYear() ? e1 : e2
                ));

        return latestByUniversity.values().stream()
                .map(doc -> new UniversityAdmissionMethodSummaryResponse(
                        doc.getUniversityName(),
                        doc.getNotes()
                )).toList();
    }


    @Override
    public UniversityAdmissionMethodDetailResponse getMethodsBySchool(Integer universityId) {
        int currentYear = Year.now().getValue() + 1;
        final int MIN_VALID_YEAR = 2015;

        while (currentYear >= MIN_VALID_YEAR) {
            List<UniversityEntriesDocument> docs = universityAdmissionMethodReadRepository
                    .findByUniversityIdAndYearAndStatus(universityId, currentYear, Status.ACTIVE);

            if (!docs.isEmpty()) {
                String universityName = docs.get(0).getUniversityName();

                List<AdmissionMethodDetail> methods = docs.stream()
                        .map(doc -> new AdmissionMethodDetail(
                                doc.getMethodId(),
                                doc.getMethodName(),
                                doc.getYear(),
                                doc.getNotes(),
                                doc.getConditions(),
                                doc.getRegulations(),
                                doc.getAdmissionTime()
                        ))
                        .toList();

                return new UniversityAdmissionMethodDetailResponse(universityId, universityName, methods);
            }

            currentYear--;
        }

        return new UniversityAdmissionMethodDetailResponse(universityId, null, List.of());
    }



}
