package com.example.SBA_M.service;

import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.NewsResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FollowService {
    void followNews(UUID accountId, Long newsId);

    void unfollowNews(Long Id);

    boolean isFollowing(UUID accountId, Long newsId);

    long getFollowerCount(UUID accountId);

    long getFollowingCount(Long newsId);

    Page<AccountResponse> getFollowers(UUID accountId, int page, int size);

    Page<NewsResponse> getFollowing(Long newsId, int page, int size);
}
