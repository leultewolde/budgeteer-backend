package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.request.BudgetedIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.dto.response.BudgetedIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.model.ActualIncome;
import com.leultewolde.budgeteer_backend.model.BudgetedIncome;
import com.leultewolde.budgeteer_backend.model.Contract;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = CentralMapperConfig.class, uses = {ContractMapper.class})
public interface IncomeMapper {
    @Mapping(target = "contract.contractId", source = "contractId")
    ActualIncome toEntity(ActualIncomeRequestDTO dto);

    @Mapping(target = "incomeId", source = "incomeId")
    @Mapping(target = "contract.contractId", source = "contract.contractId")
    ActualIncomeResponseDTO toDto(ActualIncome entity);

    @Mapping(target = "contract.contractId", source = "contractId")
    BudgetedIncome toEntity(BudgetedIncomeRequestDTO dto);


    @Mapping(target = "incomeId", source = "incomeId")
    @Mapping(target = "contract.contractId", source = "contract.contractId")
    BudgetedIncomeResponseDTO toDto(BudgetedIncome entity);

    List<ActualIncome> toActualIncomeEntities(List<ActualIncomeRequestDTO> dtos);

    List<ActualIncomeResponseDTO> toActualIncomeDtos(List<ActualIncome> entities);

    List<BudgetedIncome> toBudgetedIncomeEntities(List<BudgetedIncomeRequestDTO> dtos);

    List<BudgetedIncomeResponseDTO> toBudgetedIncomeDtos(List<BudgetedIncome> entities);
}
