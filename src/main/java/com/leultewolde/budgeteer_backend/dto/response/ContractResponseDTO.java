package com.leultewolde.budgeteer_backend.dto.response;

import com.leultewolde.budgeteer_backend.model.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponseDTO {
    private Long contractId;
    private int lengthInMonths;
    private BigDecimal salaryPerHour;
    private int maxHours;
    private ContractStatus status;

    public ContractResponseDTO(int lengthInMonths, BigDecimal salaryPerHour, int maxHours, ContractStatus status) {
        this.lengthInMonths = lengthInMonths;
        this.salaryPerHour = salaryPerHour;
        this.maxHours = maxHours;
        this.status = status;
    }
}
