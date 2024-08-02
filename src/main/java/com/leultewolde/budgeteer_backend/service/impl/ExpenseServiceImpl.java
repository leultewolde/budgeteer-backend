package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.exception.expense.ExpenseNotFoundException;
import com.leultewolde.budgeteer_backend.exception.expense.ExpenseSaveException;
import com.leultewolde.budgeteer_backend.mapper.ExpenseMapper;
import com.leultewolde.budgeteer_backend.model.Expense;
import com.leultewolde.budgeteer_backend.repository.ExpenseRepository;
import com.leultewolde.budgeteer_backend.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public Optional<ExpenseResponseDTO> addExpense(ExpenseRequestDTO expenseRequestDTO) {
        try {
            Expense newExpense = expenseMapper.toEntity(expenseRequestDTO);
            Expense savedExpense = expenseRepository.save(newExpense);
            return Optional.of(expenseMapper.toDto(savedExpense));
        } catch (DataAccessException e) {
            throw new ExpenseSaveException("Failed to save expense: " + e.getMessage());
        }
    }

    @Override
    public List<ExpenseResponseDTO> getExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenseMapper.toDtos(expenses);
    }

    @Override
    public Optional<ExpenseResponseDTO> getExpense(Long expenseId) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        return optionalExpense.map(expenseMapper::toDto);
    }

    @Override
    public Optional<ExpenseResponseDTO> updateExpense(Long expenseId, ExpenseRequestDTO expenseRequestDTO) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {
            throw new ExpenseNotFoundException(expenseId);
        }

        Expense foundExpense = optionalExpense.get();

        foundExpense.setDate(expenseRequestDTO.getDate());
        foundExpense.setCategory(expenseRequestDTO.getCategory());
        foundExpense.setAmount(expenseRequestDTO.getAmount());

        try {
            Expense updatedExpense = expenseRepository.save(foundExpense);
            return Optional.of(expenseMapper.toDto(updatedExpense));
        } catch (DataAccessException e) {
            throw new ExpenseSaveException("Failed to update expense: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteExpense(Long expenseId) {
        try {
            if (expenseRepository.existsById(expenseId)) {
                expenseRepository.deleteById(expenseId);
                return true;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            throw new ExpenseSaveException("Failed to delete expense: " + e.getMessage());
        }
    }
}
