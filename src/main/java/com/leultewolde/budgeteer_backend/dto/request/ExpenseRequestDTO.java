package com.leultewolde.budgeteer_backend.dto.request;

import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {
    @NotNull
    private LocalDate date;
    @NotNull
    private ExpenseCategory category;
    @NotNull
    @Positive
    private BigDecimal amount;
}
