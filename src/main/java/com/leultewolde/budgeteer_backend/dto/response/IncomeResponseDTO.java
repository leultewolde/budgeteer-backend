package com.leultewolde.budgeteer_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomeResponseDTO {
    private Long incomeId;
    private ContractResponseDTO contract;
    private int hoursWorked;
    private LocalDate date;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
}
