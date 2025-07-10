package com.example.SBA_M.service.impl;

import com.example.SBA_M.dto.response.AccountResponse;
import com.example.SBA_M.dto.response.NewsResponse;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.News;
import com.example.SBA_M.entity.commands.UserFollowNews;
import com.example.SBA_M.mapper.AccountMapper;
import com.example.SBA_M.mapper.NewsMapper;
import com.example.SBA_M.repository.commands.AccountRepository;
import com.example.SBA_M.repository.commands.FollowRepository;
import com.example.SBA_M.repository.commands.NewsRepository;
import com.example.SBA_M.service.FollowService;
import com.example.SBA_M.utils.Status;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final AccountRepository accountRepository;
    private final NewsRepository newsRepository;
    private final AccountMapper accountMapper;
    private final NewsMapper newsMapper;

    @Override
    public void followNews(UUID accountId, Long newsId) {
        // Check if account and news exist
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));

        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: " + newsId));

        // Check if already following
        if (followRepository.existsByAccountIdAndNewsIdAndStatus(accountId, newsId, Status.ACTIVE)) {
            throw new IllegalStateException("Account is already following this news");
        }

        // Create follow relationship
        UserFollowNews follow = new UserFollowNews();
        follow.setAccount(account);
        follow.setNews(news);
        follow.setFollowedAt(Instant.now());

        followRepository.save(follow);
    }

    @Override
    public void unfollowNews(Long Id) {
        UserFollowNews follow = followRepository.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Follow relationship not found"));
        follow.setStatus(Status.DELETED);
        followRepository.save(follow);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(UUID accountId, Long newsId) {
        return followRepository.existsByAccountIdAndNewsIdAndStatus(accountId, newsId, Status.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long getFollowerCount(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found with id: " + accountId);
        }
        return followRepository.countByAccountIdAndStatus(accountId, Status.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long getFollowingCount(Long newsID) {
        if (!newsRepository.existsById(newsID)) {
            throw new EntityNotFoundException("Account not found with id: " + newsID);
        }
        return followRepository.countByNewsIdAndStatus(newsID, Status.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> getFollowers(UUID accountId, int page, int size) {
        if (!accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account not found with id: " + accountId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Account> followers = followRepository.findByAccountIdAndStatus(accountId, Status.ACTIVE ,pageable);

        return followers.map(accountMapper::toAccountResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NewsResponse> getFollowing(Long newsId, int page, int size) {
        if (!newsRepository.existsById(newsId)) {
            throw new EntityNotFoundException("News not found with id: " + newsId);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<News> following = followRepository.findByNewsIdAndStatus(newsId,Status.ACTIVE, pageable);

        return following.map(newsMapper::toResponse);
    }
}
