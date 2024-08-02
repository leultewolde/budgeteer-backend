package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.exception.income.IncomeNotFoundException;
import com.leultewolde.budgeteer_backend.mapper.IncomeMapper;
import com.leultewolde.budgeteer_backend.model.BudgetedIncome;
import com.leultewolde.budgeteer_backend.repository.BudgetedIncomeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BudgetedIncomeServiceImplTest {
    @Mock
    private BudgetedIncomeRepository budgetedIncomeRepository;

    @Mock
    private IncomeMapper incomeMapper;

    @InjectMocks
    private BudgetedIncomeServiceImpl budgetedIncomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBudgetedIncome_ShouldReturnBudgetedIncomeResponseDTO_WhenSuccessfullyAdded() {
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        BudgetedIncome budgetedIncome = new BudgetedIncome();
        BudgetedIncomeResponseDTO responseDTO = new BudgetedIncomeResponseDTO();

        when(incomeMapper.toEntity(any(BudgetedIncomeRequestDTO.class))).thenReturn(budgetedIncome);
        when(budgetedIncomeRepository.save(any(BudgetedIncome.class))).thenReturn(budgetedIncome);
        when(incomeMapper.toDto(any(BudgetedIncome.class))).thenReturn(responseDTO);

        Optional<BudgetedIncomeResponseDTO> result = budgetedIncomeService.addBudgetedIncome(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(budgetedIncomeRepository, times(1)).save(budgetedIncome);
    }

    @Test
    void getBudgetedIncomes_ShouldReturnListOfBudgetedIncomeResponseDTOs() {
        BudgetedIncome budgetedIncome = new BudgetedIncome();
        BudgetedIncomeResponseDTO responseDTO = new BudgetedIncomeResponseDTO();

        when(budgetedIncomeRepository.findAll()).thenReturn(List.of(budgetedIncome));
        when(incomeMapper.toBudgetedIncomeDtos(anyList())).thenReturn(List.of(responseDTO));

        List<BudgetedIncomeResponseDTO> result = budgetedIncomeService.getBudgetedIncomes();

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.getFirst());
    }

    @Test
    void getBudgetedIncome_ShouldReturnBudgetedIncomeResponseDTO_WhenIncomeExists() {
        Long incomeId = 1L;
        BudgetedIncome budgetedIncome = new BudgetedIncome();
        BudgetedIncomeResponseDTO responseDTO = new BudgetedIncomeResponseDTO();

        when(budgetedIncomeRepository.findById(incomeId)).thenReturn(Optional.of(budgetedIncome));
        when(incomeMapper.toDto(budgetedIncome)).thenReturn(responseDTO);

        Optional<BudgetedIncomeResponseDTO> result = budgetedIncomeService.getBudgetedIncome(incomeId);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    void getBudgetedIncome_ShouldReturnEmptyOptional_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;

        when(budgetedIncomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        Optional<BudgetedIncomeResponseDTO> result = budgetedIncomeService.getBudgetedIncome(incomeId);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateBudgetedIncome_ShouldReturnUpdatedBudgetedIncomeResponseDTO_WhenIncomeExists() {
        Long incomeId = 1L;
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        BudgetedIncome existingIncome = new BudgetedIncome();
        BudgetedIncome updatedIncome = new BudgetedIncome();
        BudgetedIncomeResponseDTO responseDTO = new BudgetedIncomeResponseDTO();

        when(budgetedIncomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));
        when(budgetedIncomeRepository.save(existingIncome)).thenReturn(updatedIncome);
        when(incomeMapper.toDto(updatedIncome)).thenReturn(responseDTO);

        Optional<BudgetedIncomeResponseDTO> result = budgetedIncomeService.updateBudgetedIncome(incomeId, requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(budgetedIncomeRepository, times(1)).save(existingIncome);
    }

    @Test
    void updateBudgetedIncome_ShouldThrowException_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());

        when(budgetedIncomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        assertThrows(IncomeNotFoundException.class, () -> {
            budgetedIncomeService.updateBudgetedIncome(incomeId, requestDTO);
        });

        verify(budgetedIncomeRepository, never()).save(any(BudgetedIncome.class));
    }

    @Test
    void deleteBudgetedIncome_ShouldReturnTrue_WhenIncomeExists() {
        Long incomeId = 1L;

        when(budgetedIncomeRepository.existsById(incomeId)).thenReturn(true);

        boolean result = budgetedIncomeService.deleteBudgetedIncome(incomeId);

        assertTrue(result);
        verify(budgetedIncomeRepository, times(1)).deleteById(incomeId);
    }

    @Test
    void deleteBudgetedIncome_ShouldReturnFalse_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;

        when(budgetedIncomeRepository.existsById(incomeId)).thenReturn(false);

        boolean result = budgetedIncomeService.deleteBudgetedIncome(incomeId);

        assertFalse(result);
        verify(budgetedIncomeRepository, never()).deleteById(incomeId);
    }
}