package com.leultewolde.budgeteer_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.service.ActualIncomeService;
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

@WebMvcTest(ActualIncomeController.class)
class ActualIncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActualIncomeService actualIncomeService;

    @Autowired
    private ObjectMapper objectMapper;

    private ActualIncomeResponseDTO actualIncomeResponse;

    @BeforeEach
    void setUp() {
        ContractResponseDTO contractResponse = new ContractResponseDTO(1L, 12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
        actualIncomeResponse = new ActualIncomeResponseDTO(1L, contractResponse, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
    }

    @Test
    void addActualIncome_ShouldReturnCreatedIncome() throws Exception {
        ActualIncomeRequestDTO requestDTO = new ActualIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());

        when(actualIncomeService.addActualIncome(any(ActualIncomeRequestDTO.class))).thenReturn(Optional.of(actualIncomeResponse));

        mockMvc.perform(post("/actual-incomes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.incomeId").value(actualIncomeResponse.getIncomeId()))
                .andExpect(jsonPath("$.contract.contractId").value(actualIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$.hoursWorked").value(actualIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$.date").value(actualIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$.payPeriodStart").value(actualIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$.payPeriodEnd").value(actualIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void deleteActualIncome_ShouldReturnNoContent() throws Exception {
        when(actualIncomeService.getActualIncome(1L)).thenReturn(Optional.of(actualIncomeResponse));
        when(actualIncomeService.deleteActualIncome(1L)).thenReturn(true);

        mockMvc.perform(delete("/actual-incomes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getActualIncomes_ShouldReturnAllIncomes() throws Exception {
        when(actualIncomeService.getActualIncomes()).thenReturn(List.of(actualIncomeResponse));

        mockMvc.perform(get("/actual-incomes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].incomeId").value(actualIncomeResponse.getIncomeId()))
                .andExpect(jsonPath("$[0].contract.contractId").value(actualIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$[0].hoursWorked").value(actualIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$[0].date").value(actualIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$[0].payPeriodStart").value(actualIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$[0].payPeriodEnd").value(actualIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void getActualIncome_ShouldReturnIncome() throws Exception {
        when(actualIncomeService.getActualIncome(1L)).thenReturn(Optional.of(actualIncomeResponse));

        mockMvc.perform(get("/actual-incomes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incomeId").value(actualIncomeResponse.getIncomeId())) // Change from $.id to $.incomeId
                .andExpect(jsonPath("$.contract.contractId").value(actualIncomeResponse.getContract().getContractId()))
                .andExpect(jsonPath("$.hoursWorked").value(actualIncomeResponse.getHoursWorked()))
                .andExpect(jsonPath("$.date").value(actualIncomeResponse.getDate().toString()))
                .andExpect(jsonPath("$.payPeriodStart").value(actualIncomeResponse.getPayPeriodStart().toString()))
                .andExpect(jsonPath("$.payPeriodEnd").value(actualIncomeResponse.getPayPeriodEnd().toString()));
    }

    @Test
    void updateActualIncome_ShouldReturnUpdatedIncome() throws Exception {
        ActualIncomeRequestDTO updateDTO = new ActualIncomeRequestDTO(1L, 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));
        ActualIncomeResponseDTO updatedResponse = new ActualIncomeResponseDTO(1L, actualIncomeResponse.getContract(), 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7));

        when(actualIncomeService.updateActualIncome(1L, updateDTO)).thenReturn(Optional.of(updatedResponse));

        mockMvc.perform(put("/actual-incomes/1")
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