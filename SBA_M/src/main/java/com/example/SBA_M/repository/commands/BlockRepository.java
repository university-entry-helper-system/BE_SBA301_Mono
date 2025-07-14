package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Block;
import com.example.SBA_M.utils.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    Page<Block> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);
    
    boolean existsByName(String name);
} 