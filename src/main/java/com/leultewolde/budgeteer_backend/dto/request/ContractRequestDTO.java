package com.leultewolde.budgeteer_backend.dto.request;

import com.leultewolde.budgeteer_backend.model.ContractStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequestDTO {
    @Positive
    private int lengthInMonths;

    @NotNull
    @Positive
    private BigDecimal salaryPerHour;

    @Positive
    private int maxHours;

    @NotNull
    private ContractStatus status;
}
