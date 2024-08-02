package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.exception.contract.ContractNotFoundException;
import com.leultewolde.budgeteer_backend.mapper.ContractMapper;
import com.leultewolde.budgeteer_backend.model.Contract;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ContractMapper contractMapper;

    @InjectMocks
    private ContractServiceImpl contractService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addContract_ShouldReturnContractResponseDTO_WhenSuccessfullyAdded() {
        ContractRequestDTO requestDTO = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        Contract contract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractResponseDTO responseDTO = new ContractResponseDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);

        when(contractMapper.toEntity(any(ContractRequestDTO.class))).thenReturn(contract);
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(contractMapper.toDto(any(Contract.class))).thenReturn(responseDTO);

        Optional<ContractResponseDTO> result = contractService.addContract(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void getContracts_ShouldReturnListOfContractResponseDTOs() {
        Contract contract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractResponseDTO responseDTO = new ContractResponseDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);

        when(contractRepository.findAll()).thenReturn(List.of(contract));
        when(contractMapper.toDtos(anyList())).thenReturn(List.of(responseDTO));

        List<ContractResponseDTO> result = contractService.getContracts();

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.getFirst());
    }

    @Test
    void getContract_ShouldReturnContractResponseDTO_WhenContractExists() {
        Long contractId = 1L;
        Contract contract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractResponseDTO responseDTO = new ContractResponseDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contractMapper.toDto(contract)).thenReturn(responseDTO);

        Optional<ContractResponseDTO> result = contractService.getContract(contractId);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    void getContract_ShouldReturnEmptyOptional_WhenContractDoesNotExist() {
        Long contractId = 1L;

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        Optional<ContractResponseDTO> result = contractService.getContract(contractId);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateContract_ShouldReturnUpdatedContractResponseDTO_WhenContractExists() {
        Long contractId = 1L;
        ContractRequestDTO requestDTO = new ContractRequestDTO(24, new BigDecimal("60.00"), 35, ContractStatus.ACTIVE);
        Contract existingContract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        Contract updatedContract = new Contract(24, new BigDecimal("60.00"), 35, ContractStatus.ACTIVE);
        ContractResponseDTO responseDTO = new ContractResponseDTO(24, new BigDecimal("60.00"), 35, ContractStatus.ACTIVE);

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(existingContract));
        when(contractRepository.save(existingContract)).thenReturn(updatedContract);
        when(contractMapper.toDto(updatedContract)).thenReturn(responseDTO);

        Optional<ContractResponseDTO> result = contractService.updateContract(contractId, requestDTO);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(contractRepository, times(1)).save(existingContract);
    }

    @Test
    void updateContract_ShouldThrowException_WhenContractDoesNotExist() {
        Long contractId = 1L;
        ContractRequestDTO requestDTO = new ContractRequestDTO(24, new BigDecimal("60.00"), 35, ContractStatus.ACTIVE);

        // Mock the repository to return Optional.empty() when findById is called
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // Expect the exception to be thrown
        assertThrows(ContractNotFoundException.class, () -> {
            contractService.updateContract(contractId, requestDTO);
        });

        // Verify that save is never called since the contract does not exist
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void deleteContract_ShouldReturnTrue_WhenContractExists() {
        Long contractId = 1L;

        when(contractRepository.existsById(contractId)).thenReturn(true);

        boolean result = contractService.deleteContract(contractId);

        assertTrue(result);
        verify(contractRepository, times(1)).deleteById(contractId);
    }

    @Test
    void deleteContract_ShouldReturnFalse_WhenContractDoesNotExist() {
        Long contractId = 1L;

        when(contractRepository.existsById(contractId)).thenReturn(false);

        boolean result = contractService.deleteContract(contractId);

        assertFalse(result);
        verify(contractRepository, never()).deleteById(contractId);
    }
}