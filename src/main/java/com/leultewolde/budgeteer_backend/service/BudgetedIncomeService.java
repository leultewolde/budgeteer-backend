package com.leultewolde.budgeteer_backend.service;

import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;

import java.util.List;
import java.util.Optional;

public interface BudgetedIncomeService {

    Optional<BudgetedIncomeResponseDTO> addBudgetedIncome(BudgetedIncomeRequestDTO incomeRequestDTO);

    List<BudgetedIncomeResponseDTO> getBudgetedIncomes();

    Optional<BudgetedIncomeResponseDTO> getBudgetedIncome(Long incomeId);

    Optional<BudgetedIncomeResponseDTO> updateBudgetedIncome(Long incomeId, BudgetedIncomeRequestDTO incomeRequestDTO);

    boolean deleteBudgetedIncome(Long incomeId);
}
