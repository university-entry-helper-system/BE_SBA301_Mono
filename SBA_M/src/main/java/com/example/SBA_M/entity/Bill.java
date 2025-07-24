package com.example.SBA_M.entity;

import com.example.SBA_M.entity.commands.AbstractEntity;
import com.example.SBA_M.entity.commands.Account;
import com.example.SBA_M.utils.BillStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "bill") // Explicitly specify the table name
public class Bill extends AbstractEntity<Long> {

    @Column(name = "bill_date", nullable = false)
    LocalDate billDate;

    @Column(name = "bill_time", nullable = false)
    Instant billTime;

    @Column(name = "total_amount", nullable = false)
    Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "bill_status", nullable = false)
    BillStatus billStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "account_id", nullable = false)
    Account user;

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Payment payment;
    @Column(name = "request_id")
    private String requestId;

//    @OneToMany(mappedBy = "bill", cascade = {
//            CascadeType.PERSIST, CascadeType.MERGE,
//            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
//    Set<Ticket> tickets;
}

