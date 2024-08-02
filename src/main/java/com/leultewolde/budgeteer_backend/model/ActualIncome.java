package com.leultewolde.budgeteer_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "actual_income")
@NoArgsConstructor
public class ActualIncome extends Income {
    public ActualIncome(Contract contract, int hoursWorked, LocalDate date, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        super(contract, hoursWorked, date, payPeriodStart, payPeriodEnd);
    }
}
