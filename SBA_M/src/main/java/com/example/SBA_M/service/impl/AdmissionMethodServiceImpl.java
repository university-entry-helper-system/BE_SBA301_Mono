package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.AdmissionMethodRequest;
import com.example.SBA_M.dto.response.AdmissionMethodResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.entity.commands.AdmissionMethod;
import com.example.SBA_M.entity.queries.AdmissionMethodDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.mapper.AdmissionMethodMapper;
import com.example.SBA_M.repository.commands.AdmissionMethodRepository;
import com.example.SBA_M.repository.queries.AdmissionMethodReadRepository;
import com.example.SBA_M.service.AdmissionMethodService;
import com.example.SBA_M.service.messaging.producer.AdmissionMethodProducer;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdmissionMethodServiceImpl implements AdmissionMethodService {

    private final AdmissionMethodProducer admissionMethodProducer;
    private final AdmissionMethodRepository admissionMethodRepository;
    private final AdmissionMethodReadRepository admissionMethodReadRepository;
    private final AdmissionMethodMapper admissionMethodMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdmissionMethodResponse createAdmissionMethod(AdmissionMethodRequest request, String username) {
        AdmissionMethod admissionMethod = admissionMethodProducer.createAdmissionMethod(request, username);
        return admissionMethodMapper.toResponse(admissionMethod);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AdmissionMethodResponse> getAllAdmissionMethods(int page, int size) {
        Page<AdmissionMethodDocument> methodPage = admissionMethodReadRepository
                .findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
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
    @PreAuthorize("hasRole('ADMIN')")
    public AdmissionMethodResponse getAdmissionMethodById(Integer id) {
        AdmissionMethodDocument am = admissionMethodReadRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        return admissionMethodMapper.toResponse(am);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public AdmissionMethodResponse updateAdmissionMethod(Integer id, AdmissionMethodRequest request, String username) {
        AdmissionMethodDocument amDoc = admissionMethodReadRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        AdmissionMethod am = admissionMethodRepository.findById(amDoc.getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        am.setName(request.getName());
        am.setDescription(request.getDescription());
        am.setStatus(Status.ACTIVE);
        am.setUpdatedBy(username);
        am.setUpdatedAt(Instant.now());
        AdmissionMethod updated = admissionMethodRepository.save(am);
        admissionMethodProducer.sendUpdateEvent(updated);
        return admissionMethodMapper.toResponse(updated);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAdmissionMethod(Integer id) {
        AdmissionMethodDocument amDoc = admissionMethodReadRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        AdmissionMethod am = admissionMethodRepository.findById(amDoc.getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_KEY));
        am.setStatus(Status.DELETED);
        admissionMethodProducer.sendDeleteEvent(am);
        admissionMethodRepository.save(am);
    }
}