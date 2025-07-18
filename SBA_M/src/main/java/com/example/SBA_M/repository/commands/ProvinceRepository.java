package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Province;
import com.example.SBA_M.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    Optional<Province> findByIdAndStatus(Integer id, Status status);

    List<Province> findAllByStatus(Status status);

    Page<Province> findByStatusAndNameContainingIgnoreCase(Status status, String name, Pageable pageable);
}
