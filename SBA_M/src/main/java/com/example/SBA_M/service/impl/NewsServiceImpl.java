package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.request.NewsRequest;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.dto.response.PageResponse;
import com.example.SBA_M.dto.response.UniversityResponse;
import com.example.SBA_M.entity.queries.NewsDocument;
import com.example.SBA_M.entity.queries.NewsSearch;
import com.example.SBA_M.exception.AppException;
import com.example.SBA_M.exception.ErrorCode;
import com.example.SBA_M.repository.elasticsearch.NewsSearchRepository;
import com.example.SBA_M.repository.queries.NewsReadRepository;
import com.example.SBA_M.service.NewsService;
import com.example.SBA_M.service.messaging.producer.NewsProducer;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {
    private final NewsProducer newsProducer;
    private final NewsReadRepository newsReadRepository;
    private final NewsSearchRepository newsSearchRepository;


    @Override
    public PageResponse<NewsResponse> getNewsPaginated(int page, int size) {
        log.info("Fetching paginated news with page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<NewsDocument> newsPage = newsReadRepository.findByStatus(Status.ACTIVE, pageable);

        List<NewsResponse> items = newsPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PageResponse.<NewsResponse>builder()
                .page(newsPage.getNumber())
                .size(newsPage.getSize())
                .totalElements(newsPage.getTotalElements())
                .totalPages(newsPage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    public NewsResponse getNewsById(Long id) {
        log.info("Fetching news with id: {}", id);
        NewsDocument news = newsReadRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("News not found with id: {}", id);
                    return new AppException(ErrorCode.NEWS_NOT_FOUND);
                });

        // Increment view count
        news.setViewCount(news.getViewCount() + 1);
        newsReadRepository.save(news);
        log.debug("Incremented view count for news id: {}", id);

        return mapToResponse(news);
    }

    @Override
    public PageResponse<NewsResponse> searchNews(String keyword, int page, int size) {
        log.info("Searching news with keyword='{}', page={}, size={}", keyword, page, size);

        // Trim the keyword to remove leading and trailing spaces
        String trimmedKeyword = keyword != null ? keyword.trim() : "";

        // Check if the keyword is empty after trimming
        if (trimmedKeyword.isEmpty()) {
            // Return empty results or all results based on your requirements
            return PageResponse.<NewsResponse>builder()
                    .page(page)
                    .size(size)
                    .totalElements(0)
                    .totalPages(0)
                    .items(List.of())
                    .build();
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<NewsSearch> searchResults = newsSearchRepository.findByContentContainingOrSummaryContainingAndStatus(
                trimmedKeyword, trimmedKeyword, Status.ACTIVE, pageable);

        List<NewsResponse> items = searchResults.getContent().stream()
                .map(newsSearch -> {
                    try {
                        NewsDocument newsDoc = newsReadRepository.findById(newsSearch.getId())
                                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
                        return mapToResponse(newsDoc);
                    } catch (NumberFormatException e) {
                        log.error("Invalid news ID format: {}", newsSearch.getId(), e);
                        throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                })
                .collect(Collectors.toList());

        return PageResponse.<NewsResponse>builder()
                .page(searchResults.getNumber())
                .size(searchResults.getSize())
                .totalElements(searchResults.getTotalElements())
                .totalPages(searchResults.getTotalPages())
                .items(items)
                .build();
    }
    @Override
    public NewsResponse createNews(NewsRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Creating news with title: {}", request.getTitle());
            // Sử dụng image từ request
            return newsProducer.createNews(request, request.getImage(), username);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error while creating news", ex);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public NewsResponse updateNews(Long id, NewsRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Updating news with ID: {}", id);
            return newsProducer.updateNews(id, request, request.getImage(), username);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error while updating news with ID: {}", id, ex);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteNews(Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            String username = authentication.getName();

            log.info("Soft deleting news with ID: {}", id);
            newsProducer.deleteNews(id, username);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error while deleting news with ID: {}", id, ex);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    private NewsResponse mapToResponse(NewsDocument news) {
        log.debug("Mapping news document to response: {}", news);

        // Create UniversityResponse if university exists
        UniversityResponse universityResponse = null;
        if (news.getUniversity() != null) {
            universityResponse = UniversityResponse.builder()
                    .id(news.getUniversity().getId())
                    .name(news.getUniversity().getName())
                    .shortName(news.getUniversity().getShortName())
                    .logoUrl(news.getUniversity().getLogoUrl())
                    .foundingYear(news.getUniversity().getFoundingYear())
                    .province(news.getUniversity().getProvince())
                    .address(news.getUniversity().getAddress())
                    .email(news.getUniversity().getEmail())
                    .phone(news.getUniversity().getPhone())
                    .website(news.getUniversity().getWebsite())
                    .description(news.getUniversity().getDescription())
                    .categoryId(news.getUniversity().getCategory() != null ? news.getUniversity().getCategory().getId() : null)
                    .status(news.getUniversity().getStatus())
                    .createdAt(news.getUniversity().getCreatedAt())
                    .createdBy(news.getUniversity().getCreatedBy())
                    .updatedAt(news.getUniversity().getUpdatedAt())
                    .updatedBy(news.getUniversity().getUpdatedBy())
                    .build();
        }

        return NewsResponse.builder()
                .id(news.getId())
                .university(universityResponse)
                .title(news.getTitle())
                .summary(news.getSummary())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .category(news.getCategory())
                .viewCount(news.getViewCount())
                .newsStatus(news.getNewsStatus())
                .publishedAt(news.getPublishedAt())
                .status(news.getStatus())
                .createdAt(news.getCreatedAt())
                .createdBy(news.getCreatedBy())
                .updatedAt(news.getUpdatedAt())
                .updatedBy(news.getUpdatedBy())
                .build();
    }

}