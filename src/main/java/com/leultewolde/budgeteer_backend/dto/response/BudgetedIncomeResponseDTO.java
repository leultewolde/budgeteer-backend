package com.leultewolde.budgeteer_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class BudgetedIncomeResponseDTO extends IncomeResponseDTO {
    public BudgetedIncomeResponseDTO(Long incomeId, ContractResponseDTO contract, int hoursWorked, LocalDate date, LocalDate payPeriodStart, LocalDate payPeriodEnd) {
        super(incomeId, contract, hoursWorked, date, payPeriodStart, payPeriodEnd);
    }

    public BudgetedIncomeResponseDTO() {
    }
}
