package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.entity.commands.UniversityAdmissionMethod;
import com.example.SBA_M.entity.queries.UniversityDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.UniversityMapper;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.repository.commands.ProvinceRepository;
import com.example.SBA_M.repository.commands.UniversityCategoryRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.repository.commands.UniversityAdmissionMethodRepository;
import com.example.SBA_M.repository.queries.UniversityReadRepository;
import com.example.SBA_M.service.UniversityService;
import com.example.SBA_M.service.messaging.producer.UniversityProducer;
import com.example.SBA_M.service.minio.MinioService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityReadRepository universityReadRepository;
    private final UniversityProducer universityProducer;
    private final UniversityMapper universityMapper;
    private final UniversityCategoryRepository universityCategoryRepository;
    private final ProvinceRepository provinceRepository;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final UniversityAdmissionMethodRepository universityAdmissionMethodRepository;
    private final MinioService minioService;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UniversityResponse> getAllUniversities(String search, int page, int size, String sort, Integer categoryId, Integer provinceId, Boolean includeCampuses) {
        log.info("Fetching universities with search: {}, page: {}, size: {}, sort: {}, categoryId: {}, provinceId: {}, includeCampuses: {}", 
                search, page, size, sort, categoryId, provinceId, includeCampuses);
        
        // Create pageable with sorting
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        }

        // Get all universities and filter in Java (similar to SubjectCombination approach)
        List<University> allUniversities = universityRepository.findAllByStatus(Status.ACTIVE);
        
        // Apply filters
        List<University> filteredUniversities = allUniversities.stream()
                .filter(u -> search == null || search.isEmpty() || 
                        u.getName().toLowerCase().contains(search.toLowerCase()) ||
                        (u.getNameEn() != null && u.getNameEn().toLowerCase().contains(search.toLowerCase())) ||
                        (u.getUniversityCode() != null && u.getUniversityCode().toLowerCase().contains(search.toLowerCase())) ||
                        (u.getShortName() != null && u.getShortName().toLowerCase().contains(search.toLowerCase())))
                .filter(u -> categoryId == null || u.getCategory().getId().equals(categoryId))
                .filter(u -> provinceId == null || u.getCampuses().stream().anyMatch(c -> c.getProvince().getId().equals(provinceId)))
                .toList();

        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, filteredUniversities.size());
        List<University> pagedUniversities = filteredUniversities.subList(start, end);

        List<UniversityResponse> items = pagedUniversities.stream()
                .map(u -> universityMapper.toResponse(u, includeCampuses))
                .toList();

        return PageResponse.<UniversityResponse>builder()
                .page(page)
                .size(size)
                .totalElements((long) filteredUniversities.size())
                .totalPages((int) Math.ceil((double) filteredUniversities.size() / size))
                .items(items)
                .build();
    }

    @Override
    public University saveUniversity(University university) {
        return universityRepository.save(university);
    }

    @Override
    public UniversityResponse getUniversityById(Integer id) {
        University university = universityRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        return universityMapper.toResponse(university);
    }

    @Override
    @Transactional
    public UniversityResponse createUniversity(UniversityRequest request) {
        String username = getCurrentUsername();

        University university = new University();
        // Handle empty strings - convert to null
        university.setUniversityCode(request.getUniversityCode() != null && !request.getUniversityCode().trim().isEmpty() ? request.getUniversityCode().trim() : null);
        university.setNameEn(request.getNameEn() != null && !request.getNameEn().trim().isEmpty() ? request.getNameEn().trim() : null);
        university.setName(request.getName().trim());
        university.setShortName(request.getShortName() != null && !request.getShortName().trim().isEmpty() ? request.getShortName().trim() : null);
        university.setFanpage(request.getFanpage() != null && !request.getFanpage().trim().isEmpty() ? request.getFanpage().trim() : null);
        university.setFoundingYear(request.getFoundingYear());
        university.setEmail(request.getEmail() != null && !request.getEmail().trim().isEmpty() ? request.getEmail().trim() : null);
        university.setPhone(request.getPhone() != null && !request.getPhone().trim().isEmpty() ? request.getPhone().trim() : null);
        university.setWebsite(request.getWebsite() != null && !request.getWebsite().trim().isEmpty() ? request.getWebsite().trim() : null);
        university.setDescription(request.getDescription() != null && !request.getDescription().trim().isEmpty() ? request.getDescription().trim() : null);
        university.setStatus(Status.ACTIVE);
        university.setCreatedBy(username);
        university.setCreatedAt(Instant.now());
        university.setUpdatedBy(username);
        university.setUpdatedAt(Instant.now());

        university.setCategory(universityCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_CATEGORY_NOT_FOUND)));

        // Upload logo file nếu có
        String logoUrl = null;
        MultipartFile logoFile = request.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            logoUrl = minioService.uploadFileAndGetPresignedUrl(logoFile);
            university.setLogoUrl(logoUrl); // Lưu URL public luôn
        }

        // Save
        University saved = universityRepository.save(university);

        // --- Save admission methods ---
        if (request.getAdmissionMethodIds() != null) {
            for (Integer methodId : request.getAdmissionMethodIds()) {
                AdmissionMethod method = admissionMethodRepository.findById(methodId)
                        .orElseThrow(() -> new AppException(ErrorCode.ADMISSION_METHOD_NOT_FOUND));
                UniversityAdmissionMethod uam = new UniversityAdmissionMethod();
                uam.setUniversity(saved);
                uam.setAdmissionMethod(method);
                uam.setYear(saved.getFoundingYear() != null ? saved.getFoundingYear() : 2024); // Default to current year if founding year is null
                universityAdmissionMethodRepository.save(uam);
            }
        }

        // Produce Kafka event
        universityProducer.sendUniversityCreated(saved);

        UniversityResponse response = universityMapper.toResponse(saved);
        response.setLogoUrl(saved.getLogoUrl()); // Đảm bảo trả về URL public
        
        // Manually set admissionMethodIds from request
        if (request.getAdmissionMethodIds() != null && !request.getAdmissionMethodIds().isEmpty()) {
            response.setAdmissionMethodIds(request.getAdmissionMethodIds());
        }
        
        return response;
    }

    @Override
    @Transactional
    public UniversityResponse updateUniversity(Integer id, UniversityRequest request) {
        String username = getCurrentUsername();

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        boolean nameChanged = !university.getName().equals(request.getName());

        university.setUpdatedAt(Instant.now());

        // Handle empty strings - convert to null
        university.setUniversityCode(request.getUniversityCode() != null && !request.getUniversityCode().trim().isEmpty() ? request.getUniversityCode().trim() : null);
        university.setNameEn(request.getNameEn() != null && !request.getNameEn().trim().isEmpty() ? request.getNameEn().trim() : null);
        university.setName(request.getName().trim());
        university.setShortName(request.getShortName() != null && !request.getShortName().trim().isEmpty() ? request.getShortName().trim() : null);
        university.setFanpage(request.getFanpage() != null && !request.getFanpage().trim().isEmpty() ? request.getFanpage().trim() : null);
        university.setFoundingYear(request.getFoundingYear());
        university.setEmail(request.getEmail() != null && !request.getEmail().trim().isEmpty() ? request.getEmail().trim() : null);
        university.setPhone(request.getPhone() != null && !request.getPhone().trim().isEmpty() ? request.getPhone().trim() : null);
        university.setWebsite(request.getWebsite() != null && !request.getWebsite().trim().isEmpty() ? request.getWebsite().trim() : null);
        university.setDescription(request.getDescription() != null && !request.getDescription().trim().isEmpty() ? request.getDescription().trim() : null);

        university.setCategory(universityCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_CATEGORY_NOT_FOUND)));

        // Upload logo file nếu có
        MultipartFile logoFile = request.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoUrl = minioService.uploadFileAndGetPresignedUrl(logoFile);
            university.setLogoUrl(logoUrl); // Lưu URL public luôn
        }

        // --- Update admission methods ---
        universityAdmissionMethodRepository.deleteAll(university.getAdmissionMethods());
        university.getAdmissionMethods().clear();
        if (request.getAdmissionMethodIds() != null) {
            for (Integer methodId : request.getAdmissionMethodIds()) {
                AdmissionMethod method = admissionMethodRepository.findById(methodId)
                        .orElseThrow(() -> new AppException(ErrorCode.ADMISSION_METHOD_NOT_FOUND));
                UniversityAdmissionMethod uam = new UniversityAdmissionMethod();
                uam.setUniversity(university);
                uam.setAdmissionMethod(method);
                uam.setYear(university.getFoundingYear() != null ? university.getFoundingYear() : 2024); // Default to current year if founding year is null
                universityAdmissionMethodRepository.save(uam);
            }
        }

        University updated = universityRepository.save(university);

        universityProducer.sendUniversityUpdated(updated);

        if (nameChanged) {
            universityProducer.sendUpdateEvent(updated);
        }
        if (nameChanged) {
            universityProducer.sendUpdateSearchEvent(updated);
        }

        UniversityResponse response = universityMapper.toResponse(updated);
        response.setLogoUrl(updated.getLogoUrl()); // Đảm bảo trả về URL public
        
        // Manually set admissionMethodIds from request
        if (request.getAdmissionMethodIds() != null && !request.getAdmissionMethodIds().isEmpty()) {
            response.setAdmissionMethodIds(request.getAdmissionMethodIds());
        }
        
        return response;
    }

    @Override
    @Transactional
    public void deleteUniversity(Integer id) {
        String username = getCurrentUsername();

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        university.setStatus(Status.DELETED);
        university.setUpdatedBy(username);
        university.setUpdatedAt(Instant.now());
        University deleted = universityRepository.save(university);
        universityProducer.sendDeleteEvent(deleted.getId());;
        universityProducer.sendUniversityDeleted(deleted);
    }

    @Override
    @Transactional
    public UniversityResponse updateUniversityStatus(Integer id, Status status) {
        String username = getCurrentUsername();
        
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        
        university.setStatus(status);
        university.setUpdatedBy(username);
        university.setUpdatedAt(Instant.now());
        
        University updated = universityRepository.save(university);
        
        // Send events if needed
        if (status == Status.DELETED) {
            universityProducer.sendUniversityDeleted(updated);
        } else {
            universityProducer.sendUniversityUpdated(updated);
        }
        
        return universityMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversityResponse getUniversityByCode(String universityCode) {
        University university = universityRepository.findByUniversityCodeAndStatus(universityCode, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        return universityMapper.toResponse(university);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversityResponse getUniversityByName(String name) {
        University university = universityRepository.findByNameAndStatus(name, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        return universityMapper.toResponse(university);
    }

    @Override
    @Transactional(readOnly = true)
    public UniversityResponse getUniversityByShortName(String shortName) {
        University university = universityRepository.findByShortNameAndStatus(shortName, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        return universityMapper.toResponse(university);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UniversityResponse> getUniversitiesByProvince(Integer provinceId, Boolean includeMainCampusOnly, int page, int size, String sort) {
        log.info("Fetching universities by province: {}, includeMainCampusOnly: {}, page: {}, size: {}, sort: {}", 
                provinceId, includeMainCampusOnly, page, size, sort);
        
        // Create pageable with sorting
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        }

        // Get all universities and filter by province through campuses
        List<University> allUniversities = universityRepository.findAllByStatus(Status.ACTIVE);
        
        // Filter universities that have campuses in the specified province
        List<University> filteredUniversities = allUniversities.stream()
                .filter(u -> u.getCampuses() != null && u.getCampuses().stream()
                        .anyMatch(c -> c.getProvince().getId().equals(provinceId) && 
                                (!Boolean.TRUE.equals(includeMainCampusOnly) || Boolean.TRUE.equals(c.getIsMainCampus()))))
                .toList();

        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, filteredUniversities.size());
        List<University> pagedUniversities = filteredUniversities.subList(start, end);

        List<UniversityResponse> items = pagedUniversities.stream()
                .map(universityMapper::toResponse)
                .toList();

        return PageResponse.<UniversityResponse>builder()
                .page(page)
                .size(size)
                .totalElements((long) filteredUniversities.size())
                .totalPages((int) Math.ceil((double) filteredUniversities.size() / size))
                .items(items)
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
