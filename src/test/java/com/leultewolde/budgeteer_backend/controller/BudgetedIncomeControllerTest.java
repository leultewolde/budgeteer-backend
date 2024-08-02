package com.leultewolde.budgeteer_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.service.BudgetedIncomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetedIncomeController.class)
class BudgetedIncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetedIncomeService budgetedIncomeService;

    @Autowired
    private ObjectMapper objectMapper;

    private BudgetedIncomeResponseDTO budgetedIncomeResponse;

    @BeforeEach
    void setUp() {
        ContractResponseDTO contractResponse = new ContractResponseDTO(1L, 12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        budgetedIncomeResponse = new BudgetedIncomeResponseDTO(1L, contractResponse, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
    }

    @Test
    void deleteBudgetedIncome_ShouldReturnNoContent() throws Exception {
        when(budgetedIncomeService.getBudgetedIncome(1L)).thenReturn(Optional.of(budgetedIncomeResponse));
        when(budgetedIncomeService.deleteBudgetedIncome(1L)).thenReturn(true);

        mockMvc.perform(delete("/budgeted-incomes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void addBudgetedIncome_ShouldReturnCreatedIncome() throws Exception {
        BudgetedIncomeRequestDTO requestDTO = new BudgetedIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());

        when(budgetedIncomeService.addBudgetedIncome(any(BudgetedIncomeRequestDTO.class))).thenReturn(Optional.of(budgetedIncomeResponse));

        mockMvc.perform(post("/budgeted-incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.incomeId").value(budgetedIncomeResponse.getIncomeId()))
                .andExpect(jsonPath("$.contract.contractId").value(budgetedIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$.hoursWorked").value(budgetedIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$.date").value(budgetedIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$.payPeriodStart").value(budgetedIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$.payPeriodEnd").value(budgetedIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void getBudgetedIncomes_ShouldReturnAllIncomes() throws Exception {
        when(budgetedIncomeService.getBudgetedIncomes()).thenReturn(List.of(budgetedIncomeResponse));

        mockMvc.perform(get("/budgeted-incomes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].incomeId").value(budgetedIncomeResponse.getIncomeId()))
                .andExpect(jsonPath("$[0].contract.contractId").value(budgetedIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$[0].hoursWorked").value(budgetedIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$[0].date").value(budgetedIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$[0].payPeriodStart").value(budgetedIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$[0].payPeriodEnd").value(budgetedIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void getBudgetedIncome_ShouldReturnIncome() throws Exception {
        when(budgetedIncomeService.getBudgetedIncome(1L)).thenReturn(Optional.of(budgetedIncomeResponse));

        mockMvc.perform(get("/budgeted-incomes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incomeId").value(budgetedIncomeResponse.getIncomeId()))
                .andExpect(jsonPath("$.contract.contractId").value(budgetedIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$.hoursWorked").value(budgetedIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$.date").value(budgetedIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$.payPeriodStart").value(budgetedIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$.payPeriodEnd").value(budgetedIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void updateBudgetedIncome_ShouldReturnUpdatedIncome() throws Exception {
        BudgetedIncomeRequestDTO updateDTO = new BudgetedIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        BudgetedIncomeResponseDTO updatedResponse = new BudgetedIncomeResponseDTO(1L, budgetedIncomeResponse.getContract(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));

        when(budgetedIncomeService.updateBudgetedIncome(1L, updateDTO)).thenReturn(Optional.of(updatedResponse));

        mockMvc.perform(put("/budgeted-incomes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incomeId").value(updatedResponse.getIncomeId()))
                .andExpect(jsonPath("$.contract.contractId").value(updatedResponse.getContract().getContractId()))
                .andExpect(jsonPath("$.hoursWorked").value(updatedResponse.getHoursWorked()))
                .andExpect(jsonPath("$.date").value(updatedResponse.getDate().toString()))
                .andExpect(jsonPath("$.payPeriodStart").value(updatedResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$.payPeriodEnd").value(updatedResponse.getPayPeriodEnd().toString()));
    }
}