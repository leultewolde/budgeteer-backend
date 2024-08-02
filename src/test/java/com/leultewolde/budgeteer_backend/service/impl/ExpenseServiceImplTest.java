package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.exception.expense.ExpenseNotFoundException;
import com.leultewolde.budgeteer_backend.mapper.ExpenseMapper;
import com.leultewolde.budgeteer_backend.model.Expense;
import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import com.leultewolde.budgeteer_backend.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addExpense_ShouldReturnExpenseResponseDTO_WhenSuccessfullyAdded() {
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
        Expense expense = new Expense();
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        when(expenseMapper.toEntity(any(ExpenseRequestDTO.class))).thenReturn(expense);
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.toDto(any(Expense.class))).thenReturn(responseDTO);

        Optional<ExpenseResponseDTO> result = expenseService.addExpense(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    void getExpenses_ShouldReturnListOfExpenseResponseDTOs() {
        Expense expense = new Expense();
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        when(expenseRepository.findAll()).thenReturn(List.of(expense));
        when(expenseMapper.toDtos(anyList())).thenReturn(List.of(responseDTO));

        List<ExpenseResponseDTO> result = expenseService.getExpenses();

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.getFirst());
    }

    @Test
    void getExpense_ShouldReturnExpenseResponseDTO_WhenExpenseExists() {
        Long expenseId = 1L;
        Expense expense = new Expense();
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));
        when(expenseMapper.toDto(expense)).thenReturn(responseDTO);

        Optional<ExpenseResponseDTO> result = expenseService.getExpense(expenseId);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    void getExpense_ShouldReturnEmptyOptional_WhenExpenseDoesNotExist() {
        Long expenseId = 1L;

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        Optional<ExpenseResponseDTO> result = expenseService.getExpense(expenseId);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateExpense_ShouldReturnUpdatedExpenseResponseDTO_WhenExpenseExists() {
        Long expenseId = 1L;
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));
        Expense existingExpense = new Expense();
        Expense updatedExpense = new Expense();
        ExpenseResponseDTO responseDTO = new ExpenseResponseDTO();

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
        when(expenseRepository.save(existingExpense)).thenReturn(updatedExpense);
        when(expenseMapper.toDto(updatedExpense)).thenReturn(responseDTO);

        Optional<ExpenseResponseDTO> result = expenseService.updateExpense(expenseId, requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(expenseRepository, times(1)).save(existingExpense);
    }

    @Test
    void updateExpense_ShouldThrowException_WhenExpenseDoesNotExist() {
        Long expenseId = 1L;
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));

        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        assertThrows(ExpenseNotFoundException.class, () -> {
            expenseService.updateExpense(expenseId, requestDTO);
        });

        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void deleteExpense_ShouldReturnTrue_WhenExpenseExists() {
        Long expenseId = 1L;

        when(expenseRepository.existsById(expenseId)).thenReturn(true);

        boolean result = expenseService.deleteExpense(expenseId);

        assertTrue(result);
        verify(expenseRepository, times(1)).deleteById(expenseId);
    }

    @Test
    void deleteExpense_ShouldReturnFalse_WhenExpenseDoesNotExist() {
        Long expenseId = 1L;

        when(expenseRepository.existsById(expenseId)).thenReturn(false);

        boolean result = expenseService.deleteExpense(expenseId);

        assertFalse(result);
        verify(expenseRepository, never()).deleteById(expenseId);
    }
}