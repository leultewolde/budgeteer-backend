package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.exception.income.IncomeNotFoundException;
import com.leultewolde.budgeteer_backend.mapper.IncomeMapper;
import com.leultewolde.budgeteer_backend.model.ActualIncome;
import com.leultewolde.budgeteer_backend.repository.ActualIncomeRepository;
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

class ActualIncomeServiceImplTest {
    @Mock
    private ActualIncomeRepository actualIncomeRepository;

    @Mock
    private IncomeMapper incomeMapper;

    @InjectMocks
    private ActualIncomeServiceImpl actualIncomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addActualIncome_ShouldReturnActualIncomeResponseDTO_WhenSuccessfullyAdded() {
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncome actualIncome = new ActualIncome();
        ActualIncomeResponseDTO responseDTO = new ActualIncomeResponseDTO();

        when(incomeMapper.toEntity(any(ActualIncomeRequestDTO.class))).thenReturn(actualIncome);
        when(actualIncomeRepository.save(any(ActualIncome.class))).thenReturn(actualIncome);
        when(incomeMapper.toDto(any(ActualIncome.class))).thenReturn(responseDTO);

        Optional<ActualIncomeResponseDTO> result = actualIncomeService.addActualIncome(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(actualIncomeRepository, times(1)).save(actualIncome);
    }

    @Test
    void getActualIncomes_ShouldReturnListOfActualIncomeResponseDTOs() {
        ActualIncome actualIncome = new ActualIncome();
        ActualIncomeResponseDTO responseDTO = new ActualIncomeResponseDTO();

        when(actualIncomeRepository.findAll()).thenReturn(List.of(actualIncome));
        when(incomeMapper.toActualIncomeDtos(anyList())).thenReturn(List.of(responseDTO));

        List<ActualIncomeResponseDTO> result = actualIncomeService.getActualIncomes();

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    @Test
    void getActualIncome_ShouldReturnActualIncomeResponseDTO_WhenIncomeExists() {
        Long incomeId = 1L;
        ActualIncome actualIncome = new ActualIncome();
        ActualIncomeResponseDTO responseDTO = new ActualIncomeResponseDTO();

        when(actualIncomeRepository.findById(incomeId)).thenReturn(Optional.of(actualIncome));
        when(incomeMapper.toDto(actualIncome)).thenReturn(responseDTO);

        Optional<ActualIncomeResponseDTO> result = actualIncomeService.getActualIncome(incomeId);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    void getActualIncome_ShouldReturnEmptyOptional_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;

        when(actualIncomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        Optional<ActualIncomeResponseDTO> result = actualIncomeService.getActualIncome(incomeId);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateActualIncome_ShouldReturnUpdatedActualIncomeResponseDTO_WhenIncomeExists() {
        Long incomeId = 1L;
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncome existingIncome = new ActualIncome();
        ActualIncome updatedIncome = new ActualIncome();
        ActualIncomeResponseDTO responseDTO = new ActualIncomeResponseDTO();

        when(actualIncomeRepository.findById(incomeId)).thenReturn(Optional.of(existingIncome));
        when(actualIncomeRepository.save(existingIncome)).thenReturn(updatedIncome);
        when(incomeMapper.toDto(updatedIncome)).thenReturn(responseDTO);

        Optional<ActualIncomeResponseDTO> result = actualIncomeService.updateActualIncome(incomeId, requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(actualIncomeRepository, times(1)).save(existingIncome);
    }

    @Test
    void updateActualIncome_ShouldThrowException_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());

        when(actualIncomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        assertThrows(IncomeNotFoundException.class, () -> {
            actualIncomeService.updateActualIncome(incomeId, requestDTO);
        });

        verify(actualIncomeRepository, never()).save(any(ActualIncome.class));
    }

    @Test
    void deleteActualIncome_ShouldReturnTrue_WhenIncomeExists() {
        Long incomeId = 1L;

        when(actualIncomeRepository.existsById(incomeId)).thenReturn(true);

        boolean result = actualIncomeService.deleteActualIncome(incomeId);

        assertTrue(result);
        verify(actualIncomeRepository, times(1)).deleteById(incomeId);
    }

    @Test
    void deleteActualIncome_ShouldReturnFalse_WhenIncomeDoesNotExist() {
        Long incomeId = 1L;

        when(actualIncomeRepository.existsById(incomeId)).thenReturn(false);

        boolean result = actualIncomeService.deleteActualIncome(incomeId);

        assertFalse(result);
        verify(actualIncomeRepository, never()).deleteById(incomeId);
    }
}