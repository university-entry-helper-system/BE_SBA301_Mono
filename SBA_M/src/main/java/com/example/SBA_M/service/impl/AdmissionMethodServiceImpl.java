package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AdmissionMethodMapper;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.service.AdmissionMethodService;
import com.example.SBA_M.service.messaging.producer.AdmissionMethodProducer;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionMethodServiceImpl implements AdmissionMethodService {

    private final AdmissionMethodProducer admissionMethodProducer;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final AdmissionMethodMapper admissionMethodMapper;

    @Override
    public AdmissionMethodResponse createAdmissionMethod(AdmissionMethodRequest request, String username) {
        AdmissionMethod am = new AdmissionMethod();
        am.setName(request.getName());
        am.setDescription(request.getDescription());
        am.setStatus(Status.ACTIVE);
        am.setCreatedBy(username);
        am.setUpdatedBy(username);
        am.setCreatedAt(Instant.now());
        am.setUpdatedAt(Instant.now());
        AdmissionMethod saved = admissionMethodRepository.save(am);
        admissionMethodProducer.sendCreateEvent(saved);
        return admissionMethodMapper.toResponse(saved);
    }

    @Override
    public PageResponse<AdmissionMethodResponse> getAllAdmissionMethods(String search, int page, int size, String sort) {
        Pageable pageable;
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String sortField = sortParams[0];
            Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<AdmissionMethod> methodPage = admissionMethodRepository.findByStatusAndNameContainingIgnoreCase(
            Status.ACTIVE,
            search != null ? search : "",
            pageable
        );
        return PageResponse.<AdmissionMethodResponse>builder()
                .page(methodPage.getNumber())
                .size(methodPage.getSize())
                .totalElements(methodPage.getTotalElements())
                .totalPages(methodPage.getTotalPages())
                .items(methodPage.getContent().stream()
                        .map(admissionMethodMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public AdmissionMethodResponse getAdmissionMethodById(Integer id) {
        AdmissionMethod am = admissionMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        return admissionMethodMapper.toResponse(am);
    }

    @Override
    public AdmissionMethodResponse updateAdmissionMethod(Integer id, AdmissionMethodRequest request, String username) {
        AdmissionMethod am = admissionMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        am.setName(request.getName());
        am.setDescription(request.getDescription());
        am.setStatus(Status.ACTIVE);
        am.setUpdatedBy(username);
        am.setUpdatedAt(Instant.now());
        AdmissionMethod updated = admissionMethodRepository.save(am);
        admissionMethodProducer.sendUpdateEvent(updated);
        admissionMethodProducer.sendUpdatedEvent(updated);
        return admissionMethodMapper.toResponse(updated);
    }

    @Override
    public AdmissionMethodResponse updateAdmissionMethodStatus(Integer id, Status status) {
        AdmissionMethod am = admissionMethodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        am.setStatus(status);
        am = admissionMethodRepository.save(am);
        return admissionMethodMapper.toResponse(am);
    }

    @Override
    public void deleteAdmissionMethod(Integer id) {
        AdmissionMethod am = admissionMethodRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        am.setStatus(Status.DELETED);
        admissionMethodProducer.sendDeleteEvent(am);
        admissionMethodProducer.sendDeletedEvent(am);
        admissionMethodRepository.save(am);
    }
}
