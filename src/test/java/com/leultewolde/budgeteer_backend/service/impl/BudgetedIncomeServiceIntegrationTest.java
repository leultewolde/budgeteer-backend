package com.leultewolde.budgeteer_backend.service.impl;

import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.model.Contract;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.repository.BudgetedIncomeRepository;
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
class BudgetedIncomeServiceIntegrationTest {

    @Autowired
    private BudgetedIncomeServiceImpl budgetedIncomeService;

    @Autowired
    private BudgetedIncomeRepository budgetedIncomeRepository;

    @Autowired
    private ContractRepository contractRepository;

    private Contract contract;

    @BeforeEach
    void setUp() {
        budgetedIncomeRepository.deleteAll();
        contractRepository.deleteAll();

        contract = new Contract(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        contract = contractRepository.save(contract);
    }

    @Test
    void addBudgetedIncome_ShouldPersistAndReturnBudgetedIncome() {
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        Optional<BudgetedIncomeResponseDTO> result = budgetedIncomeService.addBudgetedIncome(requestDTO);

        assertTrue(result.isPresent());
        assertEquals(requestDTO.getHoursWorked(), result.get().getHoursWorked());
        assertEquals(requestDTO.getDate(), result.get().getDate());
        assertEquals(requestDTO.getPayPeriodStart(), result.get().getPayPeriodStart());
        assertEquals(requestDTO.getPayPeriodEnd(), result.get().getPayPeriodEnd());
        assertEquals(contract.getContractId(), result.get().getContract().getContractId());
    }

    @Test
    void getBudgetedIncomes_ShouldReturnAllIncomes() {
        BudgetedIncomeRequestDTO requestDTO1 = new BudgetedIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        BudgetedIncomeRequestDTO requestDTO2 = new BudgetedIncomeRequestDTO(contract.getContractId(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));

        budgetedIncomeService.addBudgetedIncome(requestDTO1);
        budgetedIncomeService.addBudgetedIncome(requestDTO2);

        List<BudgetedIncomeResponseDTO> incomes = budgetedIncomeService.getBudgetedIncomes();
        assertEquals(2, incomes.size());
    }

    @Test
    void updateBudgetedIncome_ShouldUpdateExistingIncome() {
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        BudgetedIncomeResponseDTO createdIncome = budgetedIncomeService.addBudgetedIncome(requestDTO).get();

        BudgetedIncomeRequestDTO updateDTO = new BudgetedIncomeRequestDTO(contract.getContractId(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        Optional<BudgetedIncomeResponseDTO> updatedIncome = budgetedIncomeService.updateBudgetedIncome(createdIncome.getIncomeId(), updateDTO);

        assertTrue(updatedIncome.isPresent());
        assertEquals(updateDTO.getHoursWorked(), updatedIncome.get().getHoursWorked());
        assertEquals(updateDTO.getDate(), updatedIncome.get().getDate());
        assertEquals(updateDTO.getPayPeriodStart(), updatedIncome.get().getPayPeriodStart());
        assertEquals(updateDTO.getPayPeriodEnd(), updatedIncome.get().getPayPeriodEnd());
    }

    @Test
    void deleteBudgetedIncome_ShouldRemoveIncome() {
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(contract.getContractId(), 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        BudgetedIncomeResponseDTO createdIncome = budgetedIncomeService.addBudgetedIncome(requestDTO).get();

        budgetedIncomeService.deleteBudgetedIncome(createdIncome.getIncomeId());
        assertTrue(budgetedIncomeService.getBudgetedIncome(createdIncome.getIncomeId()).isEmpty());
    }
}