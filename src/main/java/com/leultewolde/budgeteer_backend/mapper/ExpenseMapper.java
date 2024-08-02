package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ExpenseRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ExpenseResponseDTO;
import com.leultewolde.budgeteer_backend.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface ExpenseMapper {
    Expense toEntity(ExpenseRequestDTO dto);

    @Mapping(target = "expenseId", source = "expenseId")
    ExpenseResponseDTO toDto(Expense entity);

    List<Expense> toEntities(List<ExpenseRequestDTO> dtos);

    List<ExpenseResponseDTO> toDtos(List<Expense> entities);
}
