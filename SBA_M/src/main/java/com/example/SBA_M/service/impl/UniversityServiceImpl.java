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
    public PageResponse<UniversityResponse> getAllUniversities(String search, int page, int size, String sort, Integer categoryId, Integer provinceId) {
        log.info("Fetching universities with search: {}, page: {}, size: {}, sort: {}, categoryId: {}, provinceId: {}", 
                search, page, size, sort, categoryId, provinceId);
        
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
                        (u.getShortName() != null && u.getShortName().toLowerCase().contains(search.toLowerCase())))
                .filter(u -> categoryId == null || u.getCategory().getId().equals(categoryId))
                .filter(u -> provinceId == null || u.getProvince().getId().equals(provinceId))
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
        university.setName(request.getName());
        university.setShortName(request.getShortName());
        university.setFanpage(request.getFanpage());
        university.setFoundingYear(request.getFoundingYear());
        university.setAddress(request.getAddress());
        university.setEmail(request.getEmail());
        university.setPhone(request.getPhone());
        university.setWebsite(request.getWebsite());
        university.setDescription(request.getDescription());
        university.setStatus(Status.ACTIVE);
        university.setCreatedBy(username);
        university.setCreatedAt(Instant.now());
        university.setUpdatedBy(username);
        university.setUpdatedAt(Instant.now());

        university.setCategory(universityCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_CATEGORY_NOT_FOUND)));

        university.setProvince(provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND)));

        // Upload logo file nếu có
        String logoUrl = null;
        MultipartFile logoFile = request.getLogoFile();
        if (logoFile != null && !logoFile.isEmpty()) {
            logoUrl = minioService.uploadFileAndGetPresignedUrl(logoFile);
            university.setLogoUrl(logoUrl); // Lưu URL public luôn
        }

        // Save
        University saved = universityRepository.save(university);

        // Produce Kafka event
        universityProducer.sendUniversityCreated(saved);

        UniversityResponse response = universityMapper.toResponse(saved);
        response.setLogoUrl(saved.getLogoUrl()); // Đảm bảo trả về URL public
        return response;
    }

    @Override
    @Transactional
    public UniversityResponse updateUniversity(Integer id, UniversityRequest request) {
        String username = getCurrentUsername();

        University university = universityRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));

        boolean nameChanged = !university.getName().equals(request.getName());
        boolean provinceChanged = !university.getProvince().getId().equals(request.getProvinceId());

        university.setName(request.getName());
        university.setShortName(request.getShortName());
        university.setFanpage(request.getFanpage());
        university.setFoundingYear(request.getFoundingYear());
        university.setAddress(request.getAddress());
        university.setEmail(request.getEmail());
        university.setPhone(request.getPhone());
        university.setWebsite(request.getWebsite());
        university.setDescription(request.getDescription());

        university.setCategory(universityCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_CATEGORY_NOT_FOUND)));

        university.setProvince(provinceRepository.findById(request.getProvinceId())
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND)));
        university.setUpdatedAt(Instant.now());

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
                uam.setYear(university.getFoundingYear());
                university.getAdmissionMethods().add(uam);
            }
        }

        University updated = universityRepository.save(university);

        universityProducer.sendUniversityUpdated(updated);

        if (nameChanged) {
            universityProducer.sendUpdateEvent(updated);
        }
        if (provinceChanged|| nameChanged) {
            universityProducer.sendUpdateSearchEvent(updated);
        }

        UniversityResponse response = universityMapper.toResponse(updated);
        response.setLogoUrl(updated.getLogoUrl()); // Đảm bảo trả về URL public
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

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return authentication.getName();
    }
}
