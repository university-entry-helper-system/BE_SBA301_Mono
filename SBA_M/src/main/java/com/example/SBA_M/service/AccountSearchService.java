package com.example.SBA_M.service;

import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AccountSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<Account> findWithAdvancedFilters(
            String search,
            AccountStatus status,
            Gender gender,
            Boolean isDeleted,
            Instant createdFrom,
            Instant createdTo,
            Instant loginFrom,
            Instant loginTo,
            Pageable pageable) {

        try {
            log.info("Starting advanced search with filters: search={}, status={}, gender={}, isDeleted={}, createdFrom={}, createdTo={}, loginFrom={}, loginTo={}", 
                    search, status, gender, isDeleted, createdFrom, createdTo, loginFrom, loginTo);

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Account> query = cb.createQuery(Account.class);
            Root<Account> account = query.from(Account.class);
            
            // Select only Account entity, avoid joins
            query.select(account);

            List<Predicate> predicates = new ArrayList<>();

            // Search predicate
            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                Predicate searchPredicate = cb.or(
                    cb.like(cb.lower(account.get("fullName")), searchPattern),
                    cb.like(cb.lower(account.get("email")), searchPattern),
                    cb.like(cb.lower(account.get("username")), searchPattern),
                    cb.like(cb.lower(account.get("phone")), searchPattern)
                );
                predicates.add(searchPredicate);
                log.info("Added search predicate for pattern: {}", searchPattern);
            }

            // Status predicate
            if (status != null) {
                predicates.add(cb.equal(account.get("status"), status));
                log.info("Added status predicate: {}", status);
            }

            // Gender predicate
            if (gender != null) {
                predicates.add(cb.equal(account.get("gender"), gender));
                log.info("Added gender predicate: {}", gender);
            }

            // IsDeleted predicate
            if (isDeleted != null) {
                predicates.add(cb.equal(account.get("isDeleted"), isDeleted));
                log.info("Added isDeleted predicate: {}", isDeleted);
            }

            // Created date range
            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(account.get("createdAt"), createdFrom));
                log.info("Added createdFrom predicate: {}", createdFrom);
            }
            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(account.get("createdAt"), createdTo));
                log.info("Added createdTo predicate: {}", createdTo);
            }

            // Login date range
            if (loginFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(account.get("lastLoginAt"), loginFrom));
                log.info("Added loginFrom predicate: {}", loginFrom);
            }
            if (loginTo != null) {
                predicates.add(cb.lessThanOrEqualTo(account.get("lastLoginAt"), loginTo));
                log.info("Added loginTo predicate: {}", loginTo);
            }

            // Apply all predicates
            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
                log.info("Applied {} predicates to query", predicates.size());
            } else {
                log.info("No predicates applied, returning all accounts");
            }
            
            // Add sorting
            if (pageable.getSort().isSorted()) {
                List<Order> orders = new ArrayList<>();
                pageable.getSort().forEach(sort -> {
                    if (sort.isAscending()) {
                        orders.add(cb.asc(account.get(sort.getProperty())));
                    } else {
                        orders.add(cb.desc(account.get(sort.getProperty())));
                    }
                });
                query.orderBy(orders);
                log.info("Applied sorting: {}", pageable.getSort());
            } else {
                // Default sorting by createdAt desc
                query.orderBy(cb.desc(account.get("createdAt")));
                log.info("Applied default sorting by createdAt desc");
            }

            // Execute query with pagination
            List<Account> accounts = entityManager.createQuery(query)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            log.info("Found {} accounts for current page", accounts.size());

            // Count query for total elements
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Account> countRoot = countQuery.from(Account.class);
            countQuery.select(cb.count(countRoot));
            if (!predicates.isEmpty()) {
                countQuery.where(predicates.toArray(new Predicate[0]));
            }
            Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

            log.info("Total elements found: {}", totalElements);

            return new PageImpl<>(accounts, pageable, totalElements);
            
        } catch (Exception e) {
            log.error("Error in advanced search: ", e);
            throw new RuntimeException("Advanced search failed", e);
        }
    }
} 