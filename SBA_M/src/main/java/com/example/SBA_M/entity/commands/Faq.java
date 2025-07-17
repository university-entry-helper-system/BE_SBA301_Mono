package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.FaqType;
import com.example.SBA_M.utils.Status;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "faqs")
@NoArgsConstructor
@AllArgsConstructor
public class Faq extends AbstractEntity<Long>{

    @Column(nullable = false, length = 255)
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FaqType faqType;
}