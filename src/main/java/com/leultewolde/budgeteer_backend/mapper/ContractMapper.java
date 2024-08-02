package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.Contract;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface ContractMapper {
    Contract toEntity(ContractRequestDTO dto);

    ContractResponseDTO toDto(Contract entity);

    List<Contract> toEntities(List<ContractRequestDTO> dtos);

    List<ContractResponseDTO> toDtos(List<Contract> entities);
}
