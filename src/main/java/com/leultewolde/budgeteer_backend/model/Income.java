package com.leultewolde.budgeteer_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private int hoursWorked;
    private LocalDate date;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;

    public Income(Contract contract, int hoursWorked, LocalDate date, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        this.contract = contract;
        this.hoursWorked = hoursWorked;
        this.date = date;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
    }
}
