package com.leultewolde.budgeteer_backend.service;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ActualIncomeService {
    Optional<ActualIncomeResponseDTO> addActualIncome(ActualIncomeRequestDTO incomeRequestDTO);

    List<ActualIncomeResponseDTO> getActualIncomes();

    Optional<ActualIncomeResponseDTO> getActualIncome(Long incomeId);

    Optional<ActualIncomeResponseDTO> updateActualIncome(Long incomeId, ActualIncomeRequestDTO incomeRequestDTO);

    boolean deleteActualIncome(Long incomeId);
}
