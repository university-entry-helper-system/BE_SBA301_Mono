package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.SubscriptionPackage;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPackageRepository extends JpaRepository<SubscriptionPackage, Long> {
    Optional<SubscriptionPackage> findByIdAndStatus(Long id, Status status);
    Page<SubscriptionPackage> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);
    List<SubscriptionPackage> findByStatusOrderByPriceAsc(Status status);

}
