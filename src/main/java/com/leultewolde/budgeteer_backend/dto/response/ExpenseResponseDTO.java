package com.leultewolde.budgeteer_backend.dto.response;

import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {
    private Long expenseId;
    private LocalDate date;
    private ExpenseCategory category;
    private BigDecimal amount;
}
