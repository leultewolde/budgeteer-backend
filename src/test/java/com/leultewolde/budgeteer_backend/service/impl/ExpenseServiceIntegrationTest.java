package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import com.leultewolde.budgeteer_backend.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ExpenseServiceIntegrationTest {

    @Autowired
    private ExpenseServiceImpl expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
    }

    @Test
    void addExpense_ShouldPersistAndReturnExpense() {
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
        Optional<ExpenseResponseDTO> result = expenseService.addExpense(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(requestDTO.getDate(), result.get().getDate());
        assertEquals(requestDTO.getCategory(), result.get().getCategory());
        assertEquals(requestDTO.getAmount(), result.get().getAmount());
        assertTrue(result.get().getExpenseId() > 0);  // Check if ID is set
    }

    @Test
    void getExpenses_ShouldReturnAllExpenses() {
        ExpenseRequestDTO requestDTO1 = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
        ExpenseRequestDTO requestDTO2 = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));

        expenseService.addExpense(requestDTO1);
        expenseService.addExpense(requestDTO2);

        List<ExpenseResponseDTO> expenses = expenseService.getExpenses();
        assertEquals(2, expenses.size());
    }

    @Test
    void updateExpense_ShouldUpdateExistingExpense() {
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
        ExpenseResponseDTO createdExpense = expenseService.addExpense(requestDTO).get();

        ExpenseRequestDTO updateDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));
        Optional<ExpenseResponseDTO> updatedExpense = expenseService.updateExpense(createdExpense.getExpenseId(), updateDTO);

        assertTrue(updatedExpense.isPresent());
        assertEquals(updateDTO.getDate(), updatedExpense.get().getDate());
        assertEquals(updateDTO.getCategory(), updatedExpense.get().getCategory());
        assertEquals(updateDTO.getAmount(), updatedExpense.get().getAmount());
        assertEquals(createdExpense.getExpenseId(), updatedExpense.get().getExpenseId());
    }

    @Test
    void deleteExpense_ShouldRemoveExpense() {
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
        ExpenseResponseDTO createdExpense = expenseService.addExpense(requestDTO).get();

        expenseService.deleteExpense(createdExpense.getExpenseId());
        assertTrue(expenseService.getExpense(createdExpense.getExpenseId()).isEmpty());
    }
}