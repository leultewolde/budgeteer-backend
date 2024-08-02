package com.leultewolde.budgeteer_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "budgeted_income")
@NoArgsConstructor
public class BudgetedIncome extends Income {
    public BudgetedIncome(Contract contract, int hoursWorked, LocalDate date, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        super(contract, hoursWorked, date, payPeriodStart, payPeriodEnd);
    }
}
