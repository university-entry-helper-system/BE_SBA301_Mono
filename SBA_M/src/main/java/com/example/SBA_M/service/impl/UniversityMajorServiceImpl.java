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
import com.example.SBA_M.repository.queries.UniversityMajorReadRepository;
import com.example.SBA_M.service.UniversityMajorService;
import com.example.SBA_M.service.messaging.producer.UniversityMajorProducer;
import com.example.SBA_M.utils.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityMajorServiceImpl implements UniversityMajorService {
    private static final String INDEX_NAME = "university_major_search";
    private static final String FIELD_SUBJECT_COMBINATION_ID = "subjectCombinationId";
    private static final String FIELD_MAJOR_ID = "majorId";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_PROVINCE = "province";
    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final int DEFAULT_MAX_RESULTS = 100;

    private final UniversityRepository universityRepository;
    private final MajorRepository majorRepository;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final UniversityMajorRepository universityMajorRepository;
//    private final SubjectCombinationRepository subjectCombinationRepository;

    private final UniversityMajorMapper universityMajorMapper;
    private final UniversityMajorProducer universityMajorProducer;
    private final UniversityMajorReadRepository universityMajorReadRepository;
    private final SubjectCombinationRepository subjectCombinationService;
    private final ElasticsearchClient elasticsearchClient;

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
        University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        Major major = majorRepository.findByIdAndStatus(request.getMajorId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        List<SubjectCombination> subjectCombination = subjectCombinationService.findAllById(request.getSubjectCombinationIds());
        if (subjectCombination.size() != request.getAdmissionMethodIds().size()) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Some admission methods not found");
        }
        List<AdmissionMethod> methods = admissionMethodRepository.findAllById(request.getAdmissionMethodIds());
        if (methods.size() != request.getAdmissionMethodIds().size()) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Some admission methods not found");
        }

        UniversityMajor entity = new UniversityMajor();
        entity.setUniversity(university);
        entity.setMajor(major);
        entity.setAdmissionMethods(methods);
        entity.setSubjectCombinations(subjectCombination);
        entity.setUniversityMajorName(request.getUniversityMajorName());
        entity.setScore(request.getScores());
        entity.setQuota(request.getQuota());
        entity.setNotes(request.getNotes());

        UniversityMajor saved = universityMajorRepository.save(entity);
       universityMajorProducer.sendCreateEvents(saved);
        universityMajorProducer.sendSearchEvent(saved);
        return universityMajorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UniversityMajorResponse updateUniversityMajor(Integer id, UniversityMajorRequest request) {
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
        existing.setQuota(request.getQuota());
        existing.setNotes(request.getNotes());
        existing.setUniversityMajorName(request.getUniversityMajorName());

        UniversityMajor saved = universityMajorRepository.save(existing);
        universityMajorProducer.sendCreateEvents(saved);
        universityMajorProducer.sendSearchEvent(saved);
        return universityMajorMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteUniversityMajor(Integer id) {
        UniversityMajor um = universityMajorRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_MAJOR_NOT_FOUND));
        um.setStatus(Status.DELETED);
        universityMajorRepository.save(um);
        universityMajorProducer.sendSearchEvent(um);
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
                                                    null, // Optional degree
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
            Long subjectCombinationId, Long majorId, String province) throws IOException {
        validateSubjectCombinationId(subjectCombinationId);

        BoolQuery.Builder boolQuery = new BoolQuery.Builder()
                .must(m -> m.term(t -> t.field(FIELD_SUBJECT_COMBINATION_ID).value(subjectCombinationId)))
                .must(m -> m.term(t -> t.field(FIELD_STATUS).value(STATUS_ACTIVE)));

        if (majorId != null) {
            boolQuery.filter(f -> f.term(t -> t.field(FIELD_MAJOR_ID).value(majorId)));
        }

        if (isValidString(province)) {
            boolQuery.filter(f -> f.term(t -> t.field(FIELD_PROVINCE).value(province.trim())));
        }

        SearchResponse<UniversityMajorSearch> response = executeSearch(boolQuery.build());
        return response.hits().hits().stream()
                .map(hit -> {
                    UniversityMajorSearch doc = hit.source();
                    if (doc == null) {
                        return null;
                    }
                    return new UniversitySubjectCombinationSearchResponse(
                            doc.getUniversityName(),
                            doc.getUniversityMajorCountBySubjectCombination());
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<UniversityMajorSearchResponse> searchByMajor(
            Long majorId, String province, String method, Long subjectCombinationId) throws IOException {
        validateMajorId(majorId);

        BoolQuery.Builder boolQuery = new BoolQuery.Builder()
                .must(m -> m.term(t -> t.field(FIELD_MAJOR_ID).value(majorId)))
                .must(m -> m.term(t -> t.field(FIELD_STATUS).value(STATUS_ACTIVE)));

        if (isValidString(province)) {
            boolQuery.filter(f -> f.term(t -> t.field(FIELD_PROVINCE).value(province.trim())));
        }

        if (subjectCombinationId != null) {
            boolQuery.filter(f -> f.term(t -> t.field(FIELD_SUBJECT_COMBINATION_ID).value(subjectCombinationId)));
        }

        SearchResponse<UniversityMajorSearch> response = executeSearch(boolQuery.build());
        return response.hits().hits().stream()
                .map(hit -> {
                    UniversityMajorSearch doc = hit.source();
                    if (doc == null) {
                        return null;
                    }
                    return new UniversityMajorSearchResponse(
                            doc.getUniversityName(),
                            doc.getUniversityMajorCountByMajor());
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private SearchResponse<UniversityMajorSearch> executeSearch(BoolQuery boolQuery) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_NAME)
                .query(q -> q.bool(boolQuery))
                .size(DEFAULT_MAX_RESULTS));

        try {
            return elasticsearchClient.search(searchRequest, UniversityMajorSearch.class);
        } catch (IOException e) {
            throw new AppException(ErrorCode.SEARCH_FAILED, "Search operation failed: " + e.getMessage());
        }
    }
    private void validateSubjectCombinationId(Long subjectCombinationId) {
        if (subjectCombinationId == null || subjectCombinationId <= 0) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Subject combination ID must be a positive long");
        }
    }
    private boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
    private void validateMajorId(Long majorId) {
        if (majorId == null || majorId <= 0) {
            throw new AppException(ErrorCode.INVALID_PARAM, "Major ID must be a positive long");
        }
    }

}
