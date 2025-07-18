package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.CampusTypeRequest;
import com.example.SBA_M.dto.response.CampusTypeResponse;
import com.example.SBA_M.entity.commands.CampusType;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.CampusTypeMapper;
import com.example.SBA_M.repository.commands.CampusTypeRepository;
import com.example.SBA_M.service.CampusTypeService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampusTypeServiceImpl implements CampusTypeService {

    private final CampusTypeRepository campusTypeRepository;
    private final CampusTypeMapper campusTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CampusTypeResponse> getAllCampusTypes() {
        log.info("Fetching all campus types");
        List<CampusType> campusTypes = campusTypeRepository.findAllByStatus(Status.ACTIVE);
        return campusTypes.stream()
                .map(campusTypeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CampusTypeResponse getCampusTypeById(Integer id) {
        CampusType campusType = campusTypeRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
        return campusTypeMapper.toResponse(campusType);
    }

    @Override
    @Transactional
    public CampusTypeResponse createCampusType(CampusTypeRequest request) {
        log.info("Creating campus type: {}", request);
        
        // Check if name already exists
        campusTypeRepository.findByNameAndStatus(request.getName(), Status.ACTIVE)
                .ifPresent(c -> {
                    throw new AppException(ErrorCode.CAMPUS_TYPE_NAME_EXISTS);
                });
        
        CampusType campusType = campusTypeMapper.toEntity(request);
        campusType.setStatus(Status.ACTIVE);
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        campusType.setCreatedBy(currentUser);
        campusType.setUpdatedBy(currentUser);
        campusType.setCreatedAt(Instant.now());
        campusType.setUpdatedAt(Instant.now());
        
        CampusType savedCampusType = campusTypeRepository.save(campusType);
        return campusTypeMapper.toResponse(savedCampusType);
    }

    @Override
    @Transactional
    public CampusTypeResponse updateCampusType(Integer id, CampusTypeRequest request) {
        log.info("Updating campus type with id: {}, request: {}", id, request);
        
        CampusType existingCampusType = campusTypeRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
        
        // Check if name already exists (if changed)
        if (!existingCampusType.getName().equals(request.getName())) {
            campusTypeRepository.findByNameAndStatus(request.getName(), Status.ACTIVE)
                    .ifPresent(c -> {
                        throw new AppException(ErrorCode.CAMPUS_TYPE_NAME_EXISTS);
                    });
        }
        
        // Update fields
        existingCampusType.setName(request.getName());
        existingCampusType.setDescription(request.getDescription());
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        existingCampusType.setUpdatedBy(currentUser);
        existingCampusType.setUpdatedAt(Instant.now());
        
        CampusType updatedCampusType = campusTypeRepository.save(existingCampusType);
        return campusTypeMapper.toResponse(updatedCampusType);
    }

    @Override
    @Transactional
    public void deleteCampusType(Integer id) {
        log.info("Deleting campus type with id: {}", id);
        
        CampusType campusType = campusTypeRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
        
        campusType.setStatus(Status.DELETED);
        campusTypeRepository.save(campusType);
    }

    @Override
    @Transactional
    public CampusTypeResponse updateCampusTypeStatus(Integer id, Status status) {
        log.info("Updating campus type status with id: {}, status: {}", id, status);
        
        CampusType campusType = campusTypeRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.CAMPUS_TYPE_NOT_FOUND));
        
        campusType.setStatus(status);
        
        // Set audit fields
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication != null ? authentication.getName() : "system";
        campusType.setUpdatedBy(currentUser);
        campusType.setUpdatedAt(Instant.now());
        
        CampusType updatedCampusType = campusTypeRepository.save(campusType);
        return campusTypeMapper.toResponse(updatedCampusType);
    }

    @Override
    @Transactional(readOnly = true)
    public Object searchCampusTypes(String search, int page, int size, String sort) {
        // Parse sort param
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String sortDir = sortParams.length > 1 ? sortParams[1] : "asc";
        org.springframework.data.domain.Sort.Direction direction =
                sortDir.equalsIgnoreCase("desc") ? org.springframework.data.domain.Sort.Direction.DESC : org.springframework.data.domain.Sort.Direction.ASC;
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, org.springframework.data.domain.Sort.by(direction, sortBy));

        org.springframework.data.domain.Page<CampusType> campusTypePage;
        if (search != null && !search.isBlank()) {
            campusTypePage = campusTypeRepository.findByNameContainingIgnoreCaseAndStatus(search, Status.ACTIVE, pageable);
        } else {
            campusTypePage = campusTypeRepository.findAllByStatus(Status.ACTIVE, pageable);
        }
        List<CampusTypeResponse> items = campusTypePage.getContent().stream().map(campusTypeMapper::toResponse).toList();
        return java.util.Map.of(
                "page", page,
                "size", size,
                "totalElements", campusTypePage.getTotalElements(),
                "totalPages", campusTypePage.getTotalPages(),
                "items", items
        );
    }
} 