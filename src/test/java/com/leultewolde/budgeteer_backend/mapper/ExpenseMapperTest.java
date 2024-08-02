package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.model.Expense;
import com.leultewolde.budgeteer_backend.model.ExpenseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpenseMapperTest {

    private ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        expenseMapper = Mappers.getMapper(ExpenseMapper.class);
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        ExpenseRequestDTO dto = new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00"));

        Expense expense = expenseMapper.toEntity(dto);

        assertEquals(dto.getDate(), expense.getDate());
        assertEquals(dto.getCategory(), expense.getCategory());
        assertEquals(dto.getAmount(), expense.getAmount());
    }

    @Test
    void toDto_ShouldMapEntityToDTO() {
        Expense expense = new Expense();
        expense.setDate(LocalDate.now());
        expense.setCategory(ExpenseCategory.NEEDS);
        expense.setAmount(new BigDecimal("100.00"));

        ExpenseResponseDTO dto = expenseMapper.toDto(expense);

        assertEquals(expense.getDate(), dto.getDate());
        assertEquals(expense.getCategory(), dto.getCategory());
        assertEquals(expense.getAmount(), dto.getAmount());
    }

    @Test
    void toEntities_ShouldMapDTOsToEntities() {
        List<ExpenseRequestDTO> dtoList = List.of(
                new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00")),
                new ExpenseRequestDTO(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"))
        );

        List<Expense> expenses = expenseMapper.toEntities(dtoList);

        assertEquals(dtoList.size(), expenses.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getDate(), expenses.get(i).getDate());
            assertEquals(dtoList.get(i).getCategory(), expenses.get(i).getCategory());
            assertEquals(dtoList.get(i).getAmount(), expenses.get(i).getAmount());
        }
    }

    @Test
    void toDtos_ShouldMapEntitiesToDTOs() {
        List<Expense> expenses = List.of(
                new Expense(LocalDate.now(), ExpenseCategory.NEEDS, new BigDecimal("100.00")),
                new Expense(LocalDate.now(), ExpenseCategory.WANTS, new BigDecimal("50.00"))
        );

        List<ExpenseResponseDTO> dtoList = expenseMapper.toDtos(expenses);

        assertEquals(expenses.size(), dtoList.size());
        for (int i = 0; i < expenses.size(); i++) {
            assertEquals(expenses.get(i).getDate(), dtoList.get(i).getDate());
            assertEquals(expenses.get(i).getCategory(), dtoList.get(i).getCategory());
            assertEquals(expenses.get(i).getAmount(), dtoList.get(i).getAmount());
        }
    }
}