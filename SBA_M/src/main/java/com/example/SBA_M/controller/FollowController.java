package com.example.SBA_M.controller;


import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.ApiResponse;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "Follow news")
    @PostMapping("/{accountId}/news/{newsId}")
    public ApiResponse<Void> followNews(
            @PathVariable UUID accountId,
            @PathVariable Long newsId
    ) {
        followService.followNews(accountId, newsId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Followed news successfully")
                .build();
    }

    @Operation(summary = "Unfollow news by follow relationship ID")
    @DeleteMapping("/{followId}")
    public ApiResponse<Void> unfollowNews(
            @PathVariable Long followId
    ) {
        followService.unfollowNews(followId);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Unfollowed news successfully")
                .build();
    }

    @Operation(summary = "Check if account is following news")
    @GetMapping("/{accountId}/news/{newsId}/is-following")
    public ApiResponse<Boolean> isFollowing(
            @PathVariable UUID accountId,
            @PathVariable Long newsId
    ) {
        boolean isFollowing = followService.isFollowing(accountId, newsId);
        return ApiResponse.<Boolean>builder()
                .code(1000)
                .message("Follow check successful")
                .result(isFollowing)
                .build();
    }

    @Operation(summary = "Get follower count of an account")
    @GetMapping("/{accountId}/followers/count")
    public ApiResponse<Long> getFollowerCount(
            @PathVariable UUID accountId
    ) {
        long count = followService.getFollowerCount(accountId);
        return ApiResponse.<Long>builder()
                .code(1000)
                .message("Follower count retrieved successfully")
                .result(count)
                .build();
    }

    @Operation(summary = "Get following count of news")
    @GetMapping("/news/{newsId}/following/count")
    public ApiResponse<Long> getFollowingCount(
            @PathVariable Long newsId
    ) {
        long count = followService.getFollowingCount(newsId);
        return ApiResponse.<Long>builder()
                .code(1000)
                .message("Following count retrieved successfully")
                .result(count)
                .build();
    }

    @Operation(summary = "Get followers of an account")
    @GetMapping("/{accountId}/followers")
    public ApiResponse<Page<AccountResponse>> getFollowers(
            @PathVariable UUID accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AccountResponse> followers = followService.getFollowers(accountId, page, size);
        return ApiResponse.<Page<AccountResponse>>builder()
                .code(1000)
                .message("Followers fetched successfully")
                .result(followers)
                .build();
    }

    @Operation(summary = "Get news followed by an account")
    @GetMapping("/news/{newsId}/following")
    public ApiResponse<Page<NewsResponse>> getFollowing(
            @PathVariable Long newsId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NewsResponse> following = followService.getFollowing(newsId, page, size);
        return ApiResponse.<Page<NewsResponse>>builder()
                .code(1000)
                .message("Following news fetched successfully")
                .result(following)
                .build();
    }
}
