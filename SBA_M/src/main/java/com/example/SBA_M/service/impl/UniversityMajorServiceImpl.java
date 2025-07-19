package com.example.SBA_M.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.SBA_M.dto.response.UniversityMajorSearchResponse;
import com.example.SBA_M.entity.queries.UniversityMajorSearch;
import com.example.SBA_M.dto.request.UniversityMajorRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityMajorResponse;
import com.example.SBA_M.dto.response.UniversitySubjectCombinationSearchResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionResponse;
import com.example.SBA_M.dto.response.major_search_response.MajorAdmissionYearGroup;
import com.example.SBA_M.dto.response.major_search_response.MajorMethodGroup;
import com.example.SBA_M.dto.response.major_search_response.SubjectCombinationScore;
import com.example.SBA_M.dto.response.sub_combine_search_package.MajorScoreEntry;
import com.example.SBA_M.dto.response.sub_combine_search_package.MethodGroup;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationResponse;
import com.example.SBA_M.dto.response.sub_combine_search_package.SubjectCombinationYearGroup;
import com.example.SBA_M.dto.response.tuition_search_response.*;
import com.example.SBA_M.entity.commands.*;
import com.example.SBA_M.entity.queries.AdmissionEntriesDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.UniversityMajorMapper;
import com.example.SBA_M.repository.commands.*;
import com.example.SBA_M.repository.elasticsearch.UniversityMajorSearchRepository;
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.service.UniversityMajorService;
import com.example.SBA_M.service.messaging.producer.UniversityMajorProducer;
import com.example.SBA_M.utils.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Instant;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityMajorServiceImpl implements UniversityMajorService {


    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final UniversityMajorRepository universityMajorRepository;
//    private final SubjectCombinationRepository subjectCombinationRepository;

    private final UniversityMajorMapper universityMajorMapper;
    private final UniversityMajorProducer universityMajorProducer;
    private final UniversityMajorReadRepository universityMajorReadRepository;
    private final SubjectCombinationRepository subjectCombinationService;
    private final UniversityMajorSearchRepository universityMajorSearchRepository;


    @Override
    public PageResponse<UniversityMajorResponse> getAllUniversityMajors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<UniversityMajor> majorPage = universityMajorRepository.findByStatus(Status.ACTIVE, pageable);

        List<UniversityMajorResponse> items = majorPage.getContent().stream()
                .map(universityMajorMapper::toResponse)
                .toList();

        return PageResponse.<UniversityMajorResponse>builder()
                .page(majorPage.getNumber())
                .size(majorPage.getSize())
                .totalElements(majorPage.getTotalElements())
                .totalPages(majorPage.getTotalPages())
                .items(items)
                .build();
    }


    @Override
    public UniversityMajorResponse getUniversityMajorById(Integer id) {
        UniversityMajor um = universityMajorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_MAJOR_NOT_FOUND));
        return universityMajorMapper.toResponse(um);
    }

    @Override
    @Transactional
    public UniversityMajorResponse createUniversityMajor(UniversityMajorRequest request) {
        String username = getCurrentUsername();
        University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        Major major = majorRepository.findByIdAndStatus(request.getMajorId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        List<SubjectCombination> subjectCombination = subjectCombinationService.findAllById(request.getSubjectCombinationIds());
        List<AdmissionMethod> methods = admissionMethodRepository.findAllById(request.getAdmissionMethodIds());
        if (methods.size() != request.getAdmissionMethodIds().size()) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Some admission methods not found");
        }

        UniversityMajor entity = new UniversityMajor();
        entity.setUniversity(university);
        entity.setMajor(major);
        entity.setAdmissionMethods(methods);
        entity.setSubjectCombinations(subjectCombination);
        entity.setScore(request.getScores());
        entity.setYear(request.getYear());
        entity.setUniversityMajorName(request.getUniversityMajorName());
        entity.setQuota(request.getQuota());
        entity.setNotes(request.getNotes());
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedBy(username);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedBy(username);
        entity.setUpdatedAt(Instant.now());

        UniversityMajor saved = universityMajorRepository.save(entity);
         universityMajorProducer.sendCreateEvents(saved);
        universityMajorProducer.sendSearchEvent(saved,entity.getYear());
        return universityMajorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UniversityMajorResponse updateUniversityMajor(Integer id, UniversityMajorRequest request) {
        String username = getCurrentUsername();
        UniversityMajor existing = universityMajorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_MAJOR_NOT_FOUND));

        List<Integer> methodIds = request.getAdmissionMethodIds();
        List<AdmissionMethod> methods = admissionMethodRepository.findAllByIdInAndStatus(methodIds, Status.ACTIVE);
        List<SubjectCombination> subjectCombinations = subjectCombinationService.findAllById(request.getSubjectCombinationIds());
        Set<Integer> foundIds = methods.stream()
                .map(AdmissionMethod::getId)
                .collect(Collectors.toSet());

        List<Integer> missingIds = methodIds.stream()
                .filter(methodId -> !foundIds.contains(methodId))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Admission method(s) not found: " + missingIds);
        }

        existing.setAdmissionMethods(methods);
        existing.setSubjectCombinations(subjectCombinations);
        existing.setScore(request.getScores());
        existing.setYear(request.getYear());
        existing.setQuota(request.getQuota());
        existing.setNotes(request.getNotes());
        existing.setUniversityMajorName(request.getUniversityMajorName());
        existing.setStatus(Status.ACTIVE);
        existing.setUpdatedBy(username);
        existing.setUpdatedAt(Instant.now());

        UniversityMajor saved = universityMajorRepository.save(existing);
        universityMajorProducer.sendCreateEvents(saved);
        universityMajorProducer.sendSearchEvent(saved,request.getYear());
        return universityMajorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteUniversityMajor(Integer id) {
        UniversityMajor um = universityMajorRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_MAJOR_NOT_FOUND));
        um.setStatus(Status.DELETED);
        universityMajorRepository.save(um);
        universityMajorProducer.sendSearchEvent(um,um.getYear());
        universityMajorProducer.sendCreateEvents(um);
    }

    @Override
    public AdmissionUniversityTuitionResponse getAdmissionYearGroupsByUniversityId(Integer universityId) {
        List<AdmissionEntriesDocument> entries = universityMajorReadRepository
                .findByUniversityIdAndStatus(universityId, Status.ACTIVE);
        if (entries.isEmpty()) {
            return null;
        }
        String universityName = entries.getFirst().getUniversityName();

        // Group data: year -> method -> major -> subjectCombination
        Map<Integer, Map<Integer, Map<Long, List<AdmissionEntriesDocument>>>> grouped = new TreeMap<>(Collections.reverseOrder());

        for (AdmissionEntriesDocument doc : entries) {
            grouped
                    .computeIfAbsent(doc.getYear(), y -> new HashMap<>())
                    .computeIfAbsent(doc.getMethodId(), m -> new HashMap<>())
                    .computeIfAbsent(doc.getMajorId(), mj -> new ArrayList<>())
                    .add(doc);
        }

        List<AdmissionYearGroup> years = grouped.entrySet().stream()
                .map(yearEntry -> {
                    Integer year = yearEntry.getKey();

                    List<AdmissionMethodGroup> methods = yearEntry.getValue().entrySet().stream()
                            .map(methodEntry -> {
                                Integer methodId = methodEntry.getKey();
                                String methodName = methodEntry.getValue().values().stream()
                                        .flatMap(List::stream)
                                        .findFirst()
                                        .map(AdmissionEntriesDocument::getMethodName)
                                        .orElse(null);

                                List<MajorEntry> majors = methodEntry.getValue().values().stream()
                                        .map(docs -> {
                                            AdmissionEntriesDocument any = docs.getFirst();

                                            List<SubjectCombinationTuitionScore> scores = docs.stream()
                                                    .map(d -> new SubjectCombinationTuitionScore(
                                                            d.getSubjectCombination(),
                                                            d.getScore(),
                                                            d.getNote()
                                                    ))
                                                    .toList();

                                            return new MajorEntry(
                                                    any.getMajorId().toString(),
                                                    any.getMajorName(),
                                                    scores,
                                                    any.getNote()
                                            );
                                        })
                                        .toList();

                                return new AdmissionMethodGroup(
                                        methodId.toString(),
                                        methodName,
                                        majors
                                );
                            })
                            .toList();

                    return new AdmissionYearGroup(year, methods);
                })
                .toList();

        return new AdmissionUniversityTuitionResponse(
                universityId.toString(),
                universityName,
                years
        );
    }
    @Override
    public MajorAdmissionResponse getMajorAdmissionByUniversityAndMajor(Integer universityId, Long majorId) {
        List<AdmissionEntriesDocument> entries = universityMajorReadRepository
                .findByUniversityIdAndMajorIdAndStatus(universityId, majorId, Status.ACTIVE);

        if (entries.isEmpty()) {
            return new MajorAdmissionResponse(
                    String.valueOf(universityId),
                    null,
                    String.valueOf(majorId),
                    null,
                    List.of()
            );
        }

        // Extract static info from the first entry
        String universityName = entries.getFirst().getUniversityName();
        String majorName = entries.getFirst().getMajorName();

        // Grouping: year -> methodId -> methodName -> subjectCombination -> score + note
        Map<Integer, Map<Integer, Map<String, SubjectCombinationScore>>> grouped = new TreeMap<>(Collections.reverseOrder());

        for (AdmissionEntriesDocument doc : entries) {
            grouped
                    .computeIfAbsent(doc.getYear(), y -> new HashMap<>())
                    .computeIfAbsent(doc.getMethodId(), m -> new HashMap<>())
                    .put(doc.getSubjectCombination(), new SubjectCombinationScore(
                            doc.getSubjectCombination(),
                            doc.getScore(),
                            doc.getNote()
                    ));
        }

        List<MajorAdmissionYearGroup> yearGroups = grouped.entrySet().stream()
                .map(yearEntry -> {
                    Integer year = yearEntry.getKey();
                    List<MajorMethodGroup> methods = yearEntry.getValue().entrySet().stream()
                            .map(methodEntry -> {
                                Integer methodId = methodEntry.getKey();
                                String methodName = entries.stream()
                                        .filter(e -> e.getMethodId().equals(methodId))
                                        .map(AdmissionEntriesDocument::getMethodName)
                                        .findFirst()
                                        .orElse("Unknown");

                                List<SubjectCombinationScore> subjectCombinations = methodEntry.getValue().values().stream().toList();
                                return new MajorMethodGroup(
                                        String.valueOf(methodId),
                                        methodName,
                                        subjectCombinations
                                );
                            }).toList();
                    return new MajorAdmissionYearGroup(year, methods);
                }).toList();

        return new MajorAdmissionResponse(
                String.valueOf(universityId),
                universityName,
                String.valueOf(majorId),
                majorName,
                yearGroups
        );
    }
    @Override
    public SubjectCombinationResponse getSubjectCombinationAdmission(Integer universityId, Long subjectCombinationId) {
        List<AdmissionEntriesDocument> entries = universityMajorReadRepository
                .findByUniversityIdAndSubjectCombinationIdAndStatus(universityId, subjectCombinationId, Status.ACTIVE);

        if (entries.isEmpty()) {
            return null;
        }

        String universityName = entries.getFirst().getUniversityName();
        String subjectCombination = entries.getFirst().getSubjectCombination();

        // Map to avoid repeated method name lookups
        Map<Integer, String> methodNameMap = entries.stream()
                .collect(Collectors.toMap(
                        AdmissionEntriesDocument::getMethodId,
                        AdmissionEntriesDocument::getMethodName,
                        (existing, replacement) -> existing
                ));

        Map<Integer, Map<Integer, Map<String, MajorScoreEntry>>> grouped = new TreeMap<>(Collections.reverseOrder());

        for (AdmissionEntriesDocument doc : entries) {
            grouped
                    .computeIfAbsent(doc.getYear(), y -> new HashMap<>())
                    .computeIfAbsent(doc.getMethodId(), m -> new HashMap<>())
                    .put(doc.getMajorId().toString(), new MajorScoreEntry(
                            doc.getMajorId().toString(),
                            doc.getMajorName(),
                            doc.getScore(),
                            doc.getNote()
                    ));
        }

        List<SubjectCombinationYearGroup> years = grouped.entrySet().stream()
                .map(yearEntry -> {
                    Integer year = yearEntry.getKey();
                    List<MethodGroup> methods = yearEntry.getValue().entrySet().stream()
                            .map(methodEntry -> new MethodGroup(
                                    methodEntry.getKey().toString(),
                                    methodNameMap.get(methodEntry.getKey()),
                                    new ArrayList<>(methodEntry.getValue().values())
                            ))
                            .toList();

                    return new SubjectCombinationYearGroup(year, methods);
                })
                .toList();

        return new SubjectCombinationResponse(
                universityId.toString(),
                universityName,
                subjectCombination,
                years
        );
    }
    @Override
    public List<UniversitySubjectCombinationSearchResponse> searchBySubjectCombination(
            Long subjectCombinationId,
            @Nullable String universityName) throws IOException {

        int currentYear = Year.now().getValue();
        List<UniversityMajorSearch> results = new ArrayList<>();

        // Try searching for current year first, then previous years
        for (int year = currentYear; year >= currentYear - 5; year--) {
            if (universityName != null && !universityName.trim().isEmpty()) {
                results = universityMajorSearchRepository.findBySubjectCombinationIdAndUniversityNameContainingIgnoreCaseAndStatusAndYear(
                        subjectCombinationId, universityName.trim(), Status.ACTIVE, year);
            } else {
                results = universityMajorSearchRepository.findBySubjectCombinationIdAndStatusAndYear(
                        subjectCombinationId, Status.ACTIVE, year);
            }

            if (!results.isEmpty()) {
                break; // Found results for this year, stop searching
            }
        }

        return results.stream()
                .map(this::mapToSubjectCombinationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UniversityMajorSearchResponse> searchByMajor(
            Long majorId,
            @Nullable String universityName) throws IOException {

        int currentYear = Year.now().getValue();
        List<UniversityMajorSearch> results = new ArrayList<>();

        // Try searching for current year first, then previous years
        for (int year = currentYear; year >= currentYear - 5; year--) {
            if (universityName != null && !universityName.trim().isEmpty()) {
                results = universityMajorSearchRepository.findByMajorIdAndUniversityNameContainingIgnoreCaseAndStatusAndYear(
                        majorId, universityName.trim(), Status.ACTIVE, year);
            } else {
                results = universityMajorSearchRepository.findByMajorIdAndStatusAndYear(
                        majorId, Status.ACTIVE, year);
            }

            if (!results.isEmpty()) {
                break; // Found results for this year, stop searching
            }
        }

        return results.stream()
                .map(this::mapToMajorResponse)
                .collect(Collectors.toList());
    }

    private UniversitySubjectCombinationSearchResponse mapToSubjectCombinationResponse(UniversityMajorSearch entity) {
        return UniversitySubjectCombinationSearchResponse.builder()
                .universityName(entity.getUniversityName())
                .universityMajorCountBySubjectCombination(entity.getUniversityMajorCountBySubjectCombination())
                .build();
    }

    private UniversityMajorSearchResponse mapToMajorResponse(UniversityMajorSearch entity) {
        return UniversityMajorSearchResponse.builder()
                .universityName(entity.getUniversityName())
                .universityMajorCountByMajor(entity.getUniversityMajorCountByMajor())
                .build();
    }




    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return authentication.getName();
    }
}
