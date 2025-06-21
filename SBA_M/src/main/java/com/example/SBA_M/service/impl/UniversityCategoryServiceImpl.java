package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.UniversityCategoryRequest;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityCategoryResponse;
import com.example.SBA_M.entity.queries.UniversityCategoryDocument;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.queries.UniversityReadCategoryRepository;
import com.example.SBA_M.service.UniversityCategoryService;
import com.example.SBA_M.service.messaging.producer.UniversityCategoryProducer;
import com.example.SBA_M.utils.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityCategoryServiceImpl implements UniversityCategoryService {
    private final UniversityCategoryProducer categoryProducer;
    private final UniversityReadCategoryRepository categoryReadRepository;

    public PageResponse<UniversityCategoryDocument> getCategoriesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UniversityCategoryDocument> categoryPage = categoryReadRepository.findAll(pageable);
        return PageResponse.<UniversityCategoryDocument>builder()
                .page(categoryPage.getNumber())
                .size(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .items(categoryPage.getContent())
                .build();
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityCategoryResponse getCategoryById(Integer id) {
        UniversityCategoryDocument category = categoryReadRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        log.debug("Found category: {}", category);

        return mapToResponse(category);
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityCategoryResponse createCategory(UniversityCategoryRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Creating university category with name: {}", request.getName());
            return categoryProducer.createUniversityCategory(request, username);
        } catch (AuthenticationException | AppException ex) {
            log.error("Authentication error while creating category", ex);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public UniversityCategoryResponse updateCategory(Integer id, UniversityCategoryRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Updating university category with ID: {}", id);
            return categoryProducer.updateUniversityCategory(id, request, username);
        } catch (AuthenticationException | AppException ex) {
            log.error("Authentication error while updating category", ex);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        } catch (RuntimeException ex) {
            log.error("Category not found with ID: {}", id, ex);
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Soft deleting university category with ID: {}", id);
            categoryProducer.deleteUniversityCategory(id, username);
        } catch (AuthenticationException | AppException ex) {
            log.error("Authentication error while deleting category", ex);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        } catch (RuntimeException ex) {
            log.error("Category not found with ID: {}", id, ex);
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

   private UniversityCategoryResponse mapToResponse(UniversityCategoryDocument category) {
       // Add debug logging to see the actual document
       log.debug("Mapping document to response: {}", category);

       return UniversityCategoryResponse.builder()
               .id(category.getId())
               .name(category.getName())
               .description(category.getDescription())
               // Just pass the enum name as string
               .status(category.getStatus() != null ? Status.valueOf(category.getStatus().name()) : null)
               // Simple conversion from Instant to LocalDateTime
//               .createdAt(category.getCreatedAt() != null ?
//                       LocalDateTime.ofInstant(category.getCreatedAt(), ZoneId.systemDefault()) : null)
               .createdBy(category.getCreatedBy())
//               .updatedAt(category.getUpdatedAt() != null ?
//                       LocalDateTime.ofInstant(category.getUpdatedAt(), ZoneId.systemDefault()) : null)
               .updatedBy(category.getUpdatedBy())
               .build();
   }
}