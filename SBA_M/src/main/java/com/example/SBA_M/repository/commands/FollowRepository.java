package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.entity.commands.News;
import com.example.SBA_M.entity.commands.UserFollowNews;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<UserFollowNews, Long> {

    boolean existsByAccountIdAndNewsIdAndStatus(UUID accountId, Long newsId, Status status);

    long countByAccountIdAndStatus(UUID accountId, Status status);

    long countByNewsIdAndStatus(Long newsId, Status status);

    Page<Account> findByAccountIdAndStatus(UUID accountId, Status status, Pageable pageable);

    Page<News> findByNewsIdAndStatus(Long newsId, Status status, Pageable pageable);

}
