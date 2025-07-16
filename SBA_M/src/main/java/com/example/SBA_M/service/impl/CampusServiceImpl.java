package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.CampusRequest;
import com.example.SBA_M.dto.response.CampusResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.Campus;
import com.example.SBA_M.entity.commands.CampusType;
import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.entity.commands.University;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.CampusMapper;
import com.example.SBA_M.repository.commands.CampusRepository;
import com.example.SBA_M.repository.commands.CampusTypeRepository;
import com.example.SBA_M.repository.commands.ProvinceRepository;
import com.example.SBA_M.repository.commands.UniversityRepository;
import com.example.SBA_M.service.CampusService;
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

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampusServiceImpl implements CampusService {

    private final CampusRepository campusRepository;
    private final CampusMapper campusMapper;
    private final UniversityRepository universityRepository;
    private final ProvinceRepository provinceRepository;
    private final CampusTypeRepository campusTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CampusResponse> getAllCampuses(String search, int page, int size, String sort,
                                                      Integer universityId, Integer provinceId,
                                                      String campusType, Boolean isMainCampus) {
        log.info("Fetching campuses with search: {}, page: {}, size: {}, sort: {}, universityId: {}, provinceId: {}, campusType: {}, isMainCampus: {}", 
                search, page, size, sort, universityId, provinceId, campusType, isMainCampus);
        
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

        // Get all campuses and filter in Java
        List<Campus> allCampuses = campusRepository.findAllByStatus(Status.ACTIVE);
        
        // Apply filters
        List<Campus> filteredCampuses = allCampuses.stream()
                .filter(c -> search == null || search.isEmpty() || 
                        c.getCampusName().toLowerCase().contains(search.toLowerCase()) ||
                        (c.getCampusCode() != null && c.getCampusCode().toLowerCase().contains(search.toLowerCase())))
                .filter(c -> universityId == null || c.getUniversity().getId().equals(universityId))
                .filter(c -> provinceId == null || c.getProvince().getId().equals(provinceId))
                .filter(c -> campusType == null || c.getCampusType().getName().equals(campusType))
                .filter(c -> isMainCampus == null || c.getIsMainCampus().equals(isMainCampus))
                .toList();

        // Manual pagination
        int start = page * size;
        int end = Math.min(start + size, filteredCampuses.size());
        List<Campus> pagedCampuses = filteredCampuses.subList(start, end);

        List<CampusResponse> items = pagedCampuses.stream()
                .map(campusMapper::toResponse)
                .toList();

        return PageResponse.<CampusResponse>builder()
                .page(page)
                .size(size)
                .totalElements((long) filteredCampuses.size())
                .totalPages((int) Math.ceil((double) filteredCampuses.size() / size))
                .items(items)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CampusResponse getCampusById(Integer id) {
        Campus campus = campusRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_NOT_FOUND));
        return campusMapper.toResponse(campus);
    }

    @Override
    @Transactional
    public CampusResponse createCampus(CampusRequest request) {
        log.info("Creating campus: {}", request);
        
        // Validate university exists
        University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
        
        // Validate province exists
        Province province = provinceRepository.findByIdAndStatus(request.getProvinceId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));
        
        // Validate campus type exists
        CampusType campusType = campusTypeRepository.findByIdAndStatus(request.getCampusTypeId(), Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
        
        // Check if campus code is unique within university
        if (request.getCampusCode() != null && !request.getCampusCode().isEmpty()) {
            campusRepository.findByUniversityIdAndCampusCodeAndStatus(request.getUniversityId(), request.getCampusCode(), Status.ACTIVE)
                    .ifPresent(c -> {
                        throw new AppException(ErrorCode.CAMPUS_CODE_ALREADY_EXISTS);
                    });
        }
        
        // If this is main campus, ensure no other main campus exists for this university
        if (Boolean.TRUE.equals(request.getIsMainCampus())) {
            campusRepository.findByUniversityIdAndIsMainCampusAndStatus(request.getUniversityId(), true, Status.ACTIVE)
                    .forEach(c -> {
                        c.setIsMainCampus(false);
                        campusRepository.save(c);
                    });
        }
        
        Campus campus = campusMapper.toEntity(request);
        campus.setUniversity(university);
        campus.setProvince(province);
        campus.setCampusType(campusType);
        campus.setStatus(Status.ACTIVE);
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        campus.setCreatedBy(currentUser);
        campus.setUpdatedBy(currentUser);
        campus.setCreatedAt(Instant.now());
        campus.setUpdatedAt(Instant.now());
        
        Campus savedCampus = campusRepository.save(campus);
        return campusMapper.toResponse(savedCampus);
    }

    @Override
    @Transactional
    public CampusResponse updateCampus(Integer id, CampusRequest request) {
        log.info("Updating campus with id: {}, request: {}", id, request);
        
        Campus existingCampus = campusRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_NOT_FOUND));
        
        // Validate university exists if changed
        if (!existingCampus.getUniversity().getId().equals(request.getUniversityId())) {
            University university = universityRepository.findByIdAndStatus(request.getUniversityId(), Status.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.UNIVERSITY_NOT_FOUND));
            existingCampus.setUniversity(university);
        }
        
        // Validate province exists if changed
        if (!existingCampus.getProvince().getId().equals(request.getProvinceId())) {
            Province province = provinceRepository.findByIdAndStatus(request.getProvinceId(), Status.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_FOUND));
            existingCampus.setProvince(province);
        }
        
        // Check if campus code is unique within university (if changed)
        if (request.getCampusCode() != null && !request.getCampusCode().isEmpty() && 
            !request.getCampusCode().equals(existingCampus.getCampusCode())) {
            campusRepository.findByUniversityIdAndCampusCodeAndStatus(request.getUniversityId(), request.getCampusCode(), Status.ACTIVE)
                    .ifPresent(c -> {
                        throw new AppException(ErrorCode.CAMPUS_CODE_ALREADY_EXISTS);
                    });
        }
        
        // Handle main campus logic
        if (Boolean.TRUE.equals(request.getIsMainCampus()) && !Boolean.TRUE.equals(existingCampus.getIsMainCampus())) {
            campusRepository.findByUniversityIdAndIsMainCampusAndStatus(request.getUniversityId(), true, Status.ACTIVE)
                    .forEach(c -> {
                        c.setIsMainCampus(false);
                        campusRepository.save(c);
                    });
        }
        
        // Update fields
        existingCampus.setCampusName(request.getCampusName());
        existingCampus.setCampusCode(request.getCampusCode());
        existingCampus.setAddress(request.getAddress());
        existingCampus.setPhone(request.getPhone());
        existingCampus.setEmail(request.getEmail());
        existingCampus.setWebsite(request.getWebsite());
        existingCampus.setIsMainCampus(request.getIsMainCampus());
        // Validate campus type exists if changed
        if (!existingCampus.getCampusType().getId().equals(request.getCampusTypeId())) {
            CampusType campusType = campusTypeRepository.findByIdAndStatus(request.getCampusTypeId(), Status.ACTIVE)
                    .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
            existingCampus.setCampusType(campusType);
        }
        existingCampus.setDescription(request.getDescription());
        existingCampus.setEstablishedYear(request.getEstablishedYear());
        existingCampus.setAreaHectares(request.getAreaHectares());
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        existingCampus.setUpdatedBy(currentUser);
        existingCampus.setUpdatedAt(Instant.now());
        
        Campus updatedCampus = campusRepository.save(existingCampus);
        return campusMapper.toResponse(updatedCampus);
    }

    @Override
    @Transactional
    public void deleteCampus(Integer id) {
        log.info("Deleting campus with id: {}", id);
        
        Campus campus = campusRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_NOT_FOUND));
        
        campus.setStatus(Status.DELETED);
        campusRepository.save(campus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampusResponse> getCampusesByUniversity(Integer universityId, Boolean includeInactive) {
        log.info("Fetching campuses for university: {}, includeInactive: {}", universityId, includeInactive);
        
        List<Campus> campuses;
        if (Boolean.TRUE.equals(includeInactive)) {
            campuses = campusRepository.findByUniversityIdAndStatus(universityId, Status.ACTIVE);
        } else {
            campuses = campusRepository.findByUniversityIdAndStatus(universityId, Status.ACTIVE);
        }
        
        return campuses.stream()
                .map(campusMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CampusResponse> getCampusesByProvince(Integer provinceId, int page, int size, String sort) {
        log.info("Fetching campuses for province: {}, page: {}, size: {}, sort: {}", provinceId, page, size, sort);
        
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

        Page<Campus> campusPage = campusRepository.findByProvinceIdAndStatus(provinceId, Status.ACTIVE, pageable);
        
        List<CampusResponse> items = campusPage.getContent().stream()
                .map(campusMapper::toResponse)
                .toList();

        return PageResponse.<CampusResponse>builder()
                .page(page)
                .size(size)
                .totalElements(campusPage.getTotalElements())
                .totalPages(campusPage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public CampusResponse updateCampusStatus(Integer id, Status status) {
        log.info("Updating campus status with id: {}, status: {}", id, status);
        
        Campus campus = campusRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_NOT_FOUND));
        
        campus.setStatus(status);
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        campus.setUpdatedBy(currentUser);
        campus.setUpdatedAt(Instant.now());
        
        Campus updatedCampus = campusRepository.save(campus);
        return campusMapper.toResponse(updatedCampus);
    }
} 