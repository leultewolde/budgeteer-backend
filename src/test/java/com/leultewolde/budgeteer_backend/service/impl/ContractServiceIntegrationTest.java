package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.repository.ContractRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ContractServiceIntegrationTest {

    @Autowired
    private ContractServiceImpl contractService;

    @Autowired
    private ContractRepository contractRepository;

    @BeforeEach
    void setUp() {
        contractRepository.deleteAll(); // Clean up database before each test
    }

    @Test
    void addContract_ShouldPersistAndReturnContract() {
        ContractRequestDTO requestDTO = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        Optional<ContractResponseDTO> result = contractService.addContract(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(requestDTO.getLengthInMonths(), result.get().getLengthInMonths());
        assertEquals(requestDTO.getSalaryPerHour(), result.get().getSalaryPerHour());
        assertEquals(requestDTO.getMaxHours(), result.get().getMaxHours());
        assertEquals(requestDTO.getStatus(), result.get().getStatus());
    }

    @Test
    void getContracts_ShouldReturnAllContracts() {
        ContractRequestDTO requestDTO1 = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractRequestDTO requestDTO2 = new ContractRequestDTO(24, new BigDecimal("60.00"), 35, ContractStatus.INACTIVE);

        contractService.addContract(requestDTO1);
        contractService.addContract(requestDTO2);

        List<ContractResponseDTO> contracts = contractService.getContracts();
        assertEquals(2, contracts.size());
    }

    @Test
    void updateContract_ShouldUpdateExistingContract() {
        ContractRequestDTO requestDTO = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractResponseDTO createdContract = contractService.addContract(requestDTO).get();

        ContractRequestDTO updateDTO = new ContractRequestDTO(24, new BigDecimal("70.00"), 30, ContractStatus.INACTIVE);
        Optional<ContractResponseDTO> updatedContract = contractService.updateContract(createdContract.getContractId(), updateDTO);

        assertTrue(updatedContract.isPresent());
        assertEquals(updateDTO.getLengthInMonths(), updatedContract.get().getLengthInMonths());
        assertEquals(updateDTO.getSalaryPerHour(), updatedContract.get().getSalaryPerHour());
        assertEquals(updateDTO.getMaxHours(), updatedContract.get().getMaxHours());
        assertEquals(updateDTO.getStatus(), updatedContract.get().getStatus());
    }

    @Test
    void deleteContract_ShouldRemoveContract() {
        ContractRequestDTO requestDTO = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        ContractResponseDTO createdContract = contractService.addContract(requestDTO).get();

        contractService.deleteContract(createdContract.getContractId());
        assertTrue(contractService.getContract(createdContract.getContractId()).isEmpty());
    }
}