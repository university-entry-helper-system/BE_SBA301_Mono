package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.ConsultantProfileRequest;
import com.example.SBA_M.dto.response.ConsultantProfileResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.ConsultantProfile;
import com.example.SBA_M.entity.commands.Major;
import com.example.SBA_M.mapper.ConsultantProfileMapper;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.ConsultantProfileRepository;
import com.example.SBA_M.repository.commands.MajorRepository;
import com.example.SBA_M.service.ConsultantProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultantProfileServiceImpl implements ConsultantProfileService {

    private final ConsultantProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final MajorRepository majorRepository;
    private final ConsultantProfileMapper profileMapper;

    @Override
    @Transactional
    public ConsultantProfileResponse create(ConsultantProfileRequest request) {
        ConsultantProfile profile = profileMapper.toEntity(request);

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        profile.setAccount(account);

        if (request.getSpecialtyIds() != null) {
            List<Major> majors = majorRepository.findAllById(request.getSpecialtyIds());
            profile.setSpecialties(majors);
        }

        ConsultantProfile saved = profileRepository.save(profile);
        return profileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ConsultantProfileResponse update(UUID id, ConsultantProfileRequest request) {
        ConsultantProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        profile.setBio(request.getBio());
        profile.setMaxConcurrentRequests(request.getMaxConcurrentRequests());

        if (request.getSpecialtyIds() != null) {
            List<Major> majors = majorRepository.findAllById(request.getSpecialtyIds());
            profile.setSpecialties(majors);
        }

        ConsultantProfile saved = profileRepository.save(profile);
        return profileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        profileRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ConsultantProfileResponse getById(UUID id) {
        ConsultantProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultantProfileResponse> getAll(Pageable pageable) {
        return profileRepository.findAll(pageable)
                .map(profileMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConsultantProfileResponse> search(String keyword, Pageable pageable) {
        Specification<ConsultantProfile> spec = (root, query, cb) ->
                cb.like(cb.lower(root.get("bio")), "%" + keyword.toLowerCase() + "%");

        return profileRepository.findAll(spec, pageable)
                .map(profileMapper::toResponse);
    }
}
