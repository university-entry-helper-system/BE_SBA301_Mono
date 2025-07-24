package com.example.SBA_M.entity.commands;

import com.example.SBA_M.utils.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "Subscription_Package")
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPackage {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private int totalConsultations;
//
//    @ManyToMany(mappedBy = "subscriptionPackages")
//    private Set<Account> accounts = new HashSet<>();

    private Status status = Status.ACTIVE;
}
