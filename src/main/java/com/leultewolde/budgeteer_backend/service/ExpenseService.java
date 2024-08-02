package com.leultewolde.budgeteer_backend.service;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Optional<ExpenseResponseDTO> addExpense(ExpenseRequestDTO expenseRequestDTO);

    List<ExpenseResponseDTO> getExpenses();

    Optional<ExpenseResponseDTO> getExpense(Long expenseId);

    Optional<ExpenseResponseDTO> updateExpense(Long expenseId, ExpenseRequestDTO expenseRequestDTO);

    boolean deleteExpense(Long expenseId);
}
