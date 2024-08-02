package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ContractRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ContractResponseDTO;
import com.leultewolde.budgeteer_backend.model.Contract;
import com.leultewolde.budgeteer_backend.model.ContractStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContractMapperTest {
    private ContractMapper contractMapper;

    @BeforeEach
    void setUp() {
        contractMapper = Mappers.getMapper(ContractMapper.class);
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        ContractRequestDTO dto = new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE);

        Contract contract = contractMapper.toEntity(dto);

        assertEquals(dto.getLengthInMonths(), contract.getLengthInMonths());
        assertEquals(dto.getSalaryPerHour(), contract.getSalaryPerHour());
        assertEquals(dto.getMaxHours(), contract.getMaxHours());
        assertEquals(dto.getStatus(), contract.getStatus());
    }

    @Test
    void toDto_ShouldMapEntityToDTO() {
        Contract contract = new Contract();
        contract.setContractId(1L);
        contract.setLengthInMonths(12);
        contract.setSalaryPerHour(new BigDecimal("50.00"));
        contract.setMaxHours(40);
        contract.setStatus(ContractStatus.ACTIVE);

        ContractResponseDTO dto = contractMapper.toDto(contract);

        assertEquals(contract.getContractId(), dto.getContractId());  // Test contractId mapping
        assertEquals(contract.getLengthInMonths(), dto.getLengthInMonths());
        assertEquals(contract.getSalaryPerHour(), dto.getSalaryPerHour());
        assertEquals(contract.getMaxHours(), dto.getMaxHours());
        assertEquals(contract.getStatus(), dto.getStatus());
    }

    @Test
    void toEntities_ShouldMapDTOsToEntities() {
        List<ContractRequestDTO> dtoList = List.of(
                new ContractRequestDTO(12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE),
                new ContractRequestDTO(24, new BigDecimal("60.00"), 35, ContractStatus.INACTIVE)
        );

        List<Contract> contracts = contractMapper.toEntities(dtoList);

        assertEquals(dtoList.size(), contracts.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getLengthInMonths(), contracts.get(i).getLengthInMonths());
            assertEquals(dtoList.get(i).getSalaryPerHour(), contracts.get(i).getSalaryPerHour());
            assertEquals(dtoList.get(i).getMaxHours(), contracts.get(i).getMaxHours());
            assertEquals(dtoList.get(i).getStatus(), contracts.get(i).getStatus());
        }
    }

    @Test
    void toDtos_ShouldMapEntitiesToDTOs() {
        List<Contract> contracts = List.of(
                new Contract(1L, 12, new BigDecimal("50.00"), 40, ContractStatus.ACTIVE),
                new Contract(2L, 24, new BigDecimal("60.00"), 35, ContractStatus.INACTIVE)
        );

        List<ContractResponseDTO> dtoList = contractMapper.toDtos(contracts);

        assertEquals(contracts.size(), dtoList.size());
        for (int i = 0; i < contracts.size(); i++) {
            assertEquals(contracts.get(i).getContractId(), dtoList.get(i).getContractId());
            assertEquals(contracts.get(i).getLengthInMonths(), dtoList.get(i).getLengthInMonths());
            assertEquals(contracts.get(i).getSalaryPerHour(), dtoList.get(i).getSalaryPerHour());
            assertEquals(contracts.get(i).getMaxHours(), dtoList.get(i).getMaxHours());
            assertEquals(contracts.get(i).getStatus(), dtoList.get(i).getStatus());
        }
    }
}