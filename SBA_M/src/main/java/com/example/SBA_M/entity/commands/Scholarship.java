package com.example.SBA_M.entity.commands;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scholarships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ⭐ Loại giá trị học bổng: PERCENTAGE / FIXED_AMOUNT
    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", length = 20, nullable = false)
    private ValueType valueType;

    // ⭐ Giá trị: có thể là phần trăm hoặc tiền VNĐ
    @Column(name = "value_amount", nullable = false)
    private Double valueAmount;

    // ⭐ Loại điều kiện: GPA / EXAM_SCORE
    @Enumerated(EnumType.STRING)
    @Column(name = "eligibility_type", length = 20, nullable = false)
    private EligibilityType eligibilityType;

    // ⭐ Điểm tối thiểu: GPA hoặc thi THPT
    @Column(name = "min_score", nullable = false)
    private Double minScore;

    @Column(name = "application_deadline", nullable = false)
    private Instant applicationDeadline;

    @Column(name = "apply_link", length = 255)
    private String applyLink;

    @ManyToMany
    @JoinTable(
            name = "university_scholarships",
            joinColumns = @JoinColumn(name = "scholarship_id"),
            inverseJoinColumns = @JoinColumn(name = "university_id")
    )
    private List<University> universities = new ArrayList<>();

    public enum ValueType {
        PERCENTAGE, FIXED_AMOUNT
    }

    public enum EligibilityType {
        GPA, EXAM_SCORE
    }
    public String getStatus() {
        return applicationDeadline != null && applicationDeadline.isAfter(Instant .now())
                ? "ACTIVE"
                : "EXPIRED";
    }
}
