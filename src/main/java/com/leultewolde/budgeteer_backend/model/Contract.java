package com.leultewolde.budgeteer_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    private int lengthInMonths;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salaryPerHour;

    private int maxHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    public Contract(int lengthInMonths, BigDecimal salaryPerHour, int maxHours, ContractStatus status) {
        this.lengthInMonths = lengthInMonths;
        this.salaryPerHour = salaryPerHour;
        this.maxHours = maxHours;
        this.status = status;
    }
}
