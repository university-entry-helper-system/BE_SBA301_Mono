package com.example.SBA_M.repository.commands;

import com.example.SBA_M.entity.commands.Faq;
import com.example.SBA_M.utils.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {
    List<Faq> findByStatus(Status status);

}