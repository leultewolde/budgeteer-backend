package com.leultewolde.budgeteer_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import com.leultewolde.budgeteer_backend.service.ExpenseService;
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

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    private ExpenseResponseDTO expenseResponse;

    @BeforeEach
    void setUp() {
        expenseResponse = new ExpenseResponseDTO(1L, LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));
    }

    @Test
    void deleteExpense_ShouldReturnNoContent() throws Exception {
        when(expenseService.getExpense(1L)).thenReturn(Optional.of(expenseResponse));
        when(expenseService.deleteExpense(1L)).thenReturn(true);

        mockMvc.perform(delete("/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void addExpense_ShouldReturnCreatedExpense() throws Exception {
        ExpenseRequestDTO requestDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));

        when(expenseService.addExpense(any(ExpenseRequestDTO.class))).thenReturn(Optional.of(expenseResponse));

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Expect 201 Created
                .andExpect(jsonPath("$.expenseId").value(expenseResponse.getExpenseId()))
                .andExpect(jsonPath("$.date").value(expenseResponse.getDate().toString()))
                .andExpect(jsonPath("$.category").value(expenseResponse.getCategory().toString()))
                .andExpect(jsonPath("$.amount").value(expenseResponse.getAmount().doubleValue()));
    }

    @Test
    void getExpenses_ShouldReturnAllExpenses() throws Exception {
        when(expenseService.getExpenses()).thenReturn(List.of(expenseResponse));

        mockMvc.perform(get("/expenses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].expenseId").value(expenseResponse.getExpenseId()))
                .andExpect(jsonPath("$[0].date").value(expenseResponse.getDate().toString()))
                .andExpect(jsonPath("$[0].category").value(expenseResponse.getCategory().toString()))
                .andExpect(jsonPath("$[0].amount").value(expenseResponse.getAmount().doubleValue()));
    }

    @Test
    void getExpense_ShouldReturnExpense() throws Exception {
        when(expenseService.getExpense(1L)).thenReturn(Optional.of(expenseResponse));

        mockMvc.perform(get("/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenseId").value(expenseResponse.getExpenseId()))
                .andExpect(jsonPath("$.date").value(expenseResponse.getDate().toString()))
                .andExpect(jsonPath("$.category").value(expenseResponse.getCategory().toString()))
                .andExpect(jsonPath("$.amount").value(expenseResponse.getAmount().doubleValue()));
    }

    @Test
    void updateExpense_ShouldReturnUpdatedExpense() throws Exception {
        ExpenseRequestDTO updateDTO = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));
        ExpenseResponseDTO updatedResponse = new ExpenseResponseDTO(1L, LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"));

        when(expenseService.updateExpense(1L, updateDTO)).thenReturn(Optional.of(updatedResponse));

        mockMvc.perform(put("/expenses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expenseId").value(updatedResponse.getExpenseId()))
                .andExpect(jsonPath("$.date").value(updatedResponse.getDate().toString()))
                .andExpect(jsonPath("$.category").value(updatedResponse.getCategory().toString()))
                .andExpect(jsonPath("$.amount").value(updatedResponse.getAmount().doubleValue()));
    }
}