package com.leultewolde.budgeteer_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import com.leultewolde.budgeteer_backend.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractController.class)
class ContractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;

    private ContractResponseDTO contractResponse;

    @BeforeEach
    void setUp() {
        contractResponse = new ContractResponseDTO(1L, 12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);
    }

    @Test
    void addContract_ShouldReturnCreatedContract() throws Exception {
        ContractRequestDTO requestDTO = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);

        when(contractService.addContract(any(ContractRequestDTO.class))).thenReturn(Optional.of(contractResponse));

        mockMvc.perform(post("/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.contractId").value(contractResponse.getContractId()))
                .andExpect(jsonPath("$.lengthInMonths").value(contractResponse.getLengthInMonths()))
                .andExpect(jsonPath("$.salaryPerHour").value(contractResponse.getSalaryPerHour().doubleValue()))
                .andExpect(jsonPath("$.maxHours").value(contractResponse.getMaxHours()))
                .andExpect(jsonPath("$.status").value(contractResponse.getStatus().toString()));
    }

    @Test
    void getContracts_ShouldReturnAllContracts() throws Exception {
        when(contractService.getContracts()).thenReturn(List.of(contractResponse));

        mockMvc.perform(get("/contracts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contractId").value(contractResponse.getContractId()))
                .andExpect(jsonPath("$[0].lengthInMonths").value(contractResponse.getLengthInMonths()))
                .andExpect(jsonPath("$[0].salaryPerHour").value(contractResponse.getSalaryPerHour().doubleValue()))
                .andExpect(jsonPath("$[0].maxHours").value(contractResponse.getMaxHours()))
                .andExpect(jsonPath("$[0].status").value(contractResponse.getStatus().toString()));
    }

    @Test
    void getContract_ShouldReturnContract() throws Exception {
        when(contractService.getContract(1L)).thenReturn(Optional.of(contractResponse));

        mockMvc.perform(get("/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").value(contractResponse.getContractId()))
                .andExpect(jsonPath("$.lengthInMonths").value(contractResponse.getLengthInMonths()))
                .andExpect(jsonPath("$.salaryPerHour").value(contractResponse.getSalaryPerHour().doubleValue()))
                .andExpect(jsonPath("$.maxHours").value(contractResponse.getMaxHours()))
                .andExpect(jsonPath("$.status").value(contractResponse.getStatus().toString()));
    }

    @Test
    void updateContract_ShouldReturnUpdatedContract() throws Exception {
        ContractRequestDTO updateDTO = new ContractRequestDTO(24, new BigDecimal("60.00"), 35, ContractStatus.INACTIVE);
        ContractResponseDTO updatedResponse = new ContractResponseDTO(1L, 24, new BigDecimal("60.00"), 35, ContractStatus.INACTIVE);

        when(contractService.updateContract(1L, updateDTO)).thenReturn(Optional.of(updatedResponse));

        mockMvc.perform(put("/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractId").value(updatedResponse.getContractId()))
                .andExpect(jsonPath("$.lengthInMonths").value(updatedResponse.getLengthInMonths()))
                .andExpect(jsonPath("$.salaryPerHour").value(updatedResponse.getSalaryPerHour().doubleValue()))
                .andExpect(jsonPath("$.maxHours").value(updatedResponse.getMaxHours()))
                .andExpect(jsonPath("$.status").value(updatedResponse.getStatus().toString()));
    }

    @Test
    void deleteContract_ShouldReturnNoContent() throws Exception {
        // Assume the contract with ID 1 exists and is deleted successfully
        when(contractService.getContract(1L)).thenReturn(Optional.of(contractResponse));
        when(contractService.deleteContract(1L)).thenReturn(true); // Mock the boolean return

        mockMvc.perform(delete("/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Expect 204 No Content
    }
}