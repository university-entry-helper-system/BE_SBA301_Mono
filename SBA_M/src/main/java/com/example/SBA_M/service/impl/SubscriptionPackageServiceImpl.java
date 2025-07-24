package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.SubscriptionPackageRequest;
import com.example.SBA_M.dto.response.SubscriptionPackageResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.SubscriptionPackage;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.SubscriptionPackageRepository;
import com.example.SBA_M.service.SubscriptionPackageService;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPackageServiceImpl implements SubscriptionPackageService {
    private final SubscriptionPackageRepository packageRepository;
    private final AccountRepository accountRepository;

    @Override
    public SubscriptionPackageResponse createPackage(SubscriptionPackageRequest request) {
        SubscriptionPackage pkg = new SubscriptionPackage();
        pkg.setName(request.getName());
        pkg.setPrice(request.getPrice());
        pkg.setTotalConsultations(request.getTotalConsultations());
        pkg.setStatus(Status.ACTIVE);
        SubscriptionPackage saved = packageRepository.save(pkg);
        return toResponse(saved);
    }

    @Override
    public SubscriptionPackageResponse updatePackage(Long id, SubscriptionPackageRequest request) {
        SubscriptionPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Package not found"));
        pkg.setName(request.getName());
        pkg.setPrice(request.getPrice());
        pkg.setTotalConsultations(request.getTotalConsultations());
        SubscriptionPackage saved = packageRepository.save(pkg);
        return toResponse(saved);
    }

    @Override
    public void deletePackage(Long id) {
        SubscriptionPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Package not found"));
        packageRepository.delete(pkg);
    }

    @Override
    public List<SubscriptionPackageResponse> getAllPackages() {
        return packageRepository.findByStatusOrderByPriceAsc(Status.ACTIVE)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPackageResponse getPackageById(Long id) {
        SubscriptionPackage pkg = packageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Package not found"));
        return toResponse(pkg);
    }

    @Override
    @Transactional
    public void buyPackage(String username, Long packageId) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        SubscriptionPackage pkg = packageRepository.findByIdAndStatus(packageId, Status.ACTIVE)
                .orElseThrow(() -> new AppException(ErrorCode.ENTITY_NOT_FOUND, "Package not found or inactive"));
        account.setRemainingConsultations(account.getRemainingConsultations() + pkg.getTotalConsultations());
        account.getSubscriptionPackages().add(pkg);
        accountRepository.save(account);
    }

    private SubscriptionPackageResponse toResponse(SubscriptionPackage pkg) {
        SubscriptionPackageResponse resp = new SubscriptionPackageResponse();
        resp.setId(pkg.getId());
        resp.setName(pkg.getName());
        resp.setPrice(pkg.getPrice());
        resp.setTotalConsultations(pkg.getTotalConsultations());
        return resp;
    }
} 