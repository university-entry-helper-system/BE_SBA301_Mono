// Trong AccountRepository.java
package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.utils.AccountStatus;
import com.example.SBA_M.utils.Gender;
import com.example.SBA_M.utils.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhone(String phone);
    Optional<Account> findByUsernameOrEmail(String username, String email);
    Page<Account> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    
    // Methods for statistics
    Long countByStatusAndIsDeletedFalse(AccountStatus status);
    Long countByIsDeletedTrue();
    
    // Advanced search methods - Simplified version to avoid parameter type issues
    @Query("SELECT a FROM Account a WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "(a.fullName IS NOT NULL AND LOWER(a.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
           "(a.email IS NOT NULL AND LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
           "(a.username IS NOT NULL AND LOWER(a.username) LIKE LOWER(CONCAT('%', :search, '%'))) OR " +
           "(a.phone IS NOT NULL AND LOWER(a.phone) LIKE LOWER(CONCAT('%', :search, '%')))) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:gender IS NULL OR a.gender = :gender) AND " +
           "(:isDeleted IS NULL OR a.isDeleted = :isDeleted)")
    Page<Account> findWithAdvancedFilters(
            @Param("search") String search,
            @Param("status") AccountStatus status,
            @Param("gender") Gender gender,
            @Param("isDeleted") Boolean isDeleted,
            Pageable pageable);
    
    // Search by role
    @Query("SELECT a FROM Account a JOIN a.roles r WHERE r.name = :roleName")
    Page<Account> findByRoleName(@Param("roleName") RoleName roleName, Pageable pageable);
}