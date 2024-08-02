package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.model.Contract;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.repository.ActualIncomeRepository;
import com.leultewolde.budgeteer_backend.repository.ContractRepository;
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
class ActualIncomeServiceIntegrationTest {

    @Autowired
    private ActualIncomeServiceImpl actualIncomeService;

    @Autowired
    private ActualIncomeRepository actualIncomeRepository;

    @Autowired
    private ContractRepository contractRepository;

    private Contract contract;

    @BeforeEach
    void setUp() {
        actualIncomeRepository.deleteAll();
        contractRepository.deleteAll();

        contract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        contract = contractRepository.save(contract);
    }

    @Test
    void addActualIncome_ShouldPersistAndReturnActualIncome() {
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        Optional<ActualIncomeResponseDTO> result = actualIncomeService.addActualIncome(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(requestDTO.getHoursWorked(), result.get().getHoursWorked());
        assertEquals(requestDTO.getDate(), result.get().getDate());
        assertEquals(requestDTO.getPayPeriodStart(), result.get().getPayPeriodStart());
        assertEquals(requestDTO.getPayPeriodEnd(), result.get().getPayPeriodEnd());
        assertEquals(contract.getContractId(), result.get().getContract().getContractId());
    }

    @Test
    void getActualIncomes_ShouldReturnAllIncomes() {
        ActualIncomeRequestDTO requestDTO1 = new ActualIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncomeRequestDTO requestDTO2 = new ActualIncomeRequestDTO(contract.getContractId(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));

        actualIncomeService.addActualIncome(requestDTO1);
        actualIncomeService.addActualIncome(requestDTO2);

        List<ActualIncomeResponseDTO> incomes = actualIncomeService.getActualIncomes();
        assertEquals(2, incomes.size());
    }

    @Test
    void updateActualIncome_ShouldUpdateExistingIncome() {
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncomeResponseDTO createdIncome = actualIncomeService.addActualIncome(requestDTO).get();

        ActualIncomeRequestDTO updateDTO = new ActualIncomeRequestDTO(contract.getContractId(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        Optional<ActualIncomeResponseDTO> updatedIncome = actualIncomeService.updateActualIncome(createdIncome.getIncomeId(), updateDTO);

        assertTrue(updatedIncome.isPresent());
        assertEquals(updateDTO.getHoursWorked(), updatedIncome.get().getHoursWorked());
        assertEquals(updateDTO.getDate(), updatedIncome.get().getDate());
        assertEquals(updateDTO.getPayPeriodStart(), updatedIncome.get().getPayPeriodStart());
        assertEquals(updateDTO.getPayPeriodEnd(), updatedIncome.get().getPayPeriodEnd());
    }

    @Test
    void deleteActualIncome_ShouldRemoveIncome() {
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncomeResponseDTO createdIncome = actualIncomeService.addActualIncome(requestDTO).get();

        actualIncomeService.deleteActualIncome(createdIncome.getIncomeId());
        assertTrue(actualIncomeService.getActualIncome(createdIncome.getIncomeId()).isEmpty());
    }
}