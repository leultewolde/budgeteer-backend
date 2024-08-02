package com.leultewolde.budgeteer_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualIncomeRequestDTO {
    @NotNull
    private Long contractId;
    @Positive
    private int hoursWorked;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalDate payPeriodStart;
    @NotNull
    private LocalDate payPeriodEnd;
}
