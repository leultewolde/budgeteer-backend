package com.leultewolde.budgeteer_backend.mapper;

import com.leultewolde.budgeteer_backend.dto.request.ActualIncomeRequestDTO;
import com.leultewolde.budgeteer_backend.dto.response.ActualIncomeResponseDTO;
import com.leultewolde.budgeteer_backend.model.ActualIncome;
import com.leultewolde.budgeteer_backend.model.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActualIncomeMapperTest {

    private IncomeMapper incomeMapper;

    @BeforeEach
    void setUp() {
        incomeMapper = Mappers.getMapper(IncomeMapper.class);
    }

    @Test
    void toEntity_ShouldMapDTOToEntity() {
        ActualIncomeRequestDTO dto = new ActualIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now());
        ActualIncome income = incomeMapper.toEntity(dto);

        assertEquals(dto.getHoursWorked(), income.getHoursWorked());
        assertEquals(dto.getDate(), income.getDate());
        assertEquals(dto.getPayPeriodStart(), income.getPayPeriodStart());
        assertEquals(dto.getPayPeriodEnd(), income.getPayPeriodEnd());
        assertEquals(dto.getContractId(), income.getContract().getContractId());
    }

    @Test
    void toDto_ShouldMapEntityToDTO() {
        Contract contract = new Contract();
        contract.setContractId(1L);
        ActualIncome income = new ActualIncome();
        income.setContract(contract);
        income.setHoursWorked(40);
        income.setDate(LocalDate.now());
        income.setPayPeriodStart(LocalDate.now().minusDays(7));
        income.setPayPeriodEnd(LocalDate.now());

        ActualIncomeResponseDTO dto = incomeMapper.toDto(income);

        assertEquals(income.getHoursWorked(), dto.getHoursWorked());
        assertEquals(income.getDate(), dto.getDate());
        assertEquals(income.getPayPeriodStart(), dto.getPayPeriodStart());
        assertEquals(income.getPayPeriodEnd(), dto.getPayPeriodEnd());
        assertEquals(income.getContract().getContractId(), dto.getContract().getContractId());
    }

    @Test
    void toEntities_ShouldMapDTOsToEntities() {
        List<ActualIncomeRequestDTO> dtoList = List.of(
                new ActualIncomeRequestDTO(1L, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now()),
                new ActualIncomeRequestDTO(2L, 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7))
        );

        List<ActualIncome> incomes = incomeMapper.toActualIncomeEntities(dtoList);

        assertEquals(dtoList.size(), incomes.size());
        for (int i = 0; i < dtoList.size(); i++) {
            assertEquals(dtoList.get(i).getHoursWorked(), incomes.get(i).getHoursWorked());
            assertEquals(dtoList.get(i).getDate(), incomes.get(i).getDate());
            assertEquals(dtoList.get(i).getPayPeriodStart(), incomes.get(i).getPayPeriodStart());
            assertEquals(dtoList.get(i).getPayPeriodEnd(), incomes.get(i).getPayPeriodEnd());
        }
    }

    @Test
    void toDtos_ShouldMapEntitiesToDTOs() {
        Contract contract1 = new Contract();
        contract1.setContractId(1L);
        Contract contract2 = new Contract();
        contract2.setContractId(2L);

        List<ActualIncome> incomes = List.of(
                new ActualIncome(contract1, 40, LocalDate.now(), LocalDate.now().minusDays(7), LocalDate.now()),
                new ActualIncome(contract2, 35, LocalDate.now(), LocalDate.now().minusDays(14), LocalDate.now().minusDays(7))
        );

        List<ActualIncomeResponseDTO> dtoList = incomeMapper.toActualIncomeDtos(incomes);

        assertEquals(incomes.size(), dtoList.size());
        for (int i = 0; i < incomes.size(); i++) {
            assertEquals(incomes.get(i).getHoursWorked(), dtoList.get(i).getHoursWorked());
            assertEquals(incomes.get(i).getDate(), dtoList.get(i).getDate());
            assertEquals(incomes.get(i).getPayPeriodStart(), dtoList.get(i).getPayPeriodStart());
            assertEquals(incomes.get(i).getPayPeriodEnd(), dtoList.get(i).getPayPeriodEnd());
            assertEquals(incomes.get(i).getContract().getContractId(), dtoList.get(i).getContract().getContractId());
        }
    }
}